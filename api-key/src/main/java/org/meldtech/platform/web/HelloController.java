package org.meldtech.platform.web;

import jakarta.validation.Valid;
import org.meldtech.platform.model.ApiClient;
import org.meldtech.platform.model.RateLimitStatus;
import org.meldtech.platform.service.RateLimiterService;
import org.meldtech.platform.service.UsageTrackerService;
import org.meldtech.platform.storage.RedisApiKeyStore;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class HelloController {
    private final RedisApiKeyStore apiKeyStore;
    private final RateLimiterService rateLimiterService;
    private final UsageTrackerService trackerService;

    public HelloController(RedisApiKeyStore apiKeyStore,
                           RateLimiterService rateLimiterService,
                           UsageTrackerService trackerService) {
        this.apiKeyStore = apiKeyStore;
        this.rateLimiterService = rateLimiterService;
        this.trackerService = trackerService;
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

    @GetMapping( "/admin/usage/{key}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<List<Map<String, Object>>>> getRateLimitUsage(@PathVariable String key,
                                                              @RequestParam(defaultValue = "60") int minutesBack) {
        return trackerService.getUsage(key, minutesBack)
                .collectList()
                .map(ResponseEntity::ok);
    }

    @GetMapping( "/admin/usage-summary")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<List<Map<String, Object>>>> getKeyUsageSummaries() {
        return trackerService.getAllKeySummaries()
                .collectList()
                .map(ResponseEntity::ok);
    }
}
