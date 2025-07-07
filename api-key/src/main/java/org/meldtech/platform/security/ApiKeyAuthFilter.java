package org.meldtech.platform.security;

import lombok.SneakyThrows;
import org.meldtech.platform.exception.TooManyRequestException;
import org.meldtech.platform.exception.UnAuthorizedException;
import org.meldtech.platform.model.ApiClient;
import org.meldtech.platform.service.RateLimiterService;
import org.meldtech.platform.storage.RedisApiKeyStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class ApiKeyAuthFilter implements ServerSecurityContextRepository {

    @Value("${security.admin-api-key}")
    private String adminKey;

    private final RedisApiKeyStore redisApiKeyStore;
    private final RateLimiterService rateLimiterService;

    private static final String API_KEY_HEADER = "X-API-KEY";
    private static final String ADMIN_API_KEY_HEADER = "X-ADMIN-API-KEY";
    private static final String SWAGGER = "/webjars";
    private static final String SWAGGER_DOC = "/v3/api-docs";
    private static final String API_ENDPOINT = "/api";
    private static final String PUBLIC_ENDPOINT = "/public";
    private static final String ADMIN_ENDPOINT = "/admin";

    public ApiKeyAuthFilter(RedisApiKeyStore redisApiKeyStore, RateLimiterService rateLimiterService) {
        this.redisApiKeyStore = redisApiKeyStore;
        this.rateLimiterService = rateLimiterService;
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) throws UnAuthorizedException {
        ServerHttpRequest request = exchange.getRequest();
        String apiKey = request.getHeaders().getFirst(API_KEY_HEADER);
        String adminApiKey = request.getHeaders().getFirst(ADMIN_API_KEY_HEADER);
        String url = request.getURI().getPath();
        HttpMethod method = request.getMethod();

        System.out.println("Caller "+request.getURI().getPath());
        System.out.println("Method "+method);

        if((url.contains(SWAGGER_DOC) || url.contains(SWAGGER)) && apiKey == null) {
            Authentication auth = new ApiKeyAuthenticationToken(url, true);
            return Mono.just(new SecurityContextImpl(auth));
        }

        if((url.contains(API_ENDPOINT) && url.contains(PUBLIC_ENDPOINT)) ) {
            Authentication auth = new ApiKeyAuthenticationToken(url, true);
            return Mono.just(new SecurityContextImpl(auth));
        }

        if (adminKey.equals(adminApiKey) && url.contains(ADMIN_ENDPOINT) && method.equals(HttpMethod.POST)) {
            var auth = new UsernamePasswordAuthenticationToken(
                    "admin", null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
            return Mono.just(new SecurityContextImpl(auth));
        }

        if (url.contains(API_ENDPOINT) && apiKey == null) {
            throw new UnAuthorizedException("Unauthorized access to API");
        }

        System.out.println("apiKey: "+apiKey);
        return  rateLimiterService.isAllowed(apiKey)
                .doOnNext(System.out::println)
                        .flatMap(allowed ->  {
                            if (!allowed) {
                                exchange.getResponse().getHeaders().add("X-Rate-Limit-Retry-After-Seconds",
                                        Duration.ofSeconds(60).toString());
                                exchange.getResponse().getHeaders().add("Content-Type",
                                        "text/plain;charset=UTF-8");
                                exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                                throw new TooManyRequestException("Too many requests");
                            }
                            return redisApiKeyStore.validateClient(apiKey)
                                    .flatMap(this::createSecurityContext)
                                    .switchIfEmpty(Mono.error(() -> new UnAuthorizedException("Unauthorized access to API")));
                        });
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty(); // No-op
    }

    private Mono<SecurityContext> createSecurityContext(ApiClient client) {
        if(client.clientRole().equalsIgnoreCase("admin")) {
            return Mono.just(new SecurityContextImpl(createAuthToken(client)));
        }else {
            Authentication auth = new ApiKeyAuthenticationToken(client.clientApiKey(), true);
            return Mono.just(new SecurityContextImpl(auth));
        }
    }

    private UsernamePasswordAuthenticationToken createAuthToken(ApiClient client) {
            return new UsernamePasswordAuthenticationToken(
                    client.clientName(), null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }
}