package org.meldtech.platform.web;

import jakarta.validation.Valid;
import org.meldtech.platform.model.ApiClient;
import org.meldtech.platform.model.RateLimitStatus;
import org.meldtech.platform.service.RateLimiterService;
import org.meldtech.platform.storage.RedisApiKeyStore;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class HelloController {
    private final RedisApiKeyStore apiKeyStore;
    private final RateLimiterService rateLimiterService;

    public HelloController(RedisApiKeyStore apiKeyStore, RateLimiterService rateLimiterService) {
        this.apiKeyStore = apiKeyStore;
        this.rateLimiterService = rateLimiterService;
    }

    @GetMapping("/hello")
    public Mono<String> hello() {
        return Mono.just("Hello, your API key is valid!");
    }

    @GetMapping("/public/ping")
    public Mono<String> ping() {
        return Mono.just("No key needed here!");
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<String>> addKey(@RequestBody @Valid ApiClient client) {
        return apiKeyStore.addKey(client)
                .map(ok -> ResponseEntity.ok("API key added"));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<List<ApiClient>>> getAllKeys() {
        return apiKeyStore.getKey()
                .map(ResponseEntity::ok);
    }

    @GetMapping( "/admin/owner/{key}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<String>> getOwnerByKey(@PathVariable String key) {
        return apiKeyStore.getOwner(key)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/admin/{key}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<String>> deleteKey(@PathVariable String key) {
        return apiKeyStore.deleteKey(key)
                .map(deleted -> deleted ?
                        ResponseEntity.ok("Deleted") :
                        ResponseEntity.notFound().build());
    }

    @GetMapping( "/admin/rate-limit/{key}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<RateLimitStatus>> getRateLimitStatus(@PathVariable String key) {
        return rateLimiterService.getStatus(key)
                .map(ResponseEntity::ok);
    }
}
