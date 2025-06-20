package org.meldtech.platform.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ApiKeyAuthFilter implements ServerSecurityContextRepository {

    @Value("${security.api-key}")
    private String expectedApiKey;

    private static final String API_KEY_HEADER = "X-API-KEY";

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String apiKey = request.getHeaders().getFirst(API_KEY_HEADER);

        if (expectedApiKey.equals(apiKey)) {
            Authentication auth = new ApiKeyAuthenticationToken(apiKey, true);
            return Mono.just(new SecurityContextImpl(auth));
        }

        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return Mono.empty();
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty(); // No-op
    }
}