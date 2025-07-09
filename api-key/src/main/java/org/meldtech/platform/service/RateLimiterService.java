package org.meldtech.platform.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.BucketProxy;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.meldtech.platform.model.ApiClient;
import org.meldtech.platform.model.RateLimitStatus;
import org.meldtech.platform.storage.RedisApiKeyStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

//This limits each key to 100 requests per minute.
@Service
public class RateLimiterService {
    private final ProxyManager<byte[]> proxyManager;
    private final RedisApiKeyStore redisApiKeyStore;

    @Value("${rate.limit.capacity}")
    private long capacity;

    @Value("${rate.limit.durationInMinutes}")
    private long durationInMinutes;

    @Value("${rate.limit.noOfTokens}")
    private long noOfTokens;

    public RateLimiterService(ProxyManager<byte[]> proxyManager, RedisApiKeyStore redisApiKeyStore) {
        this.proxyManager = proxyManager;
        this.redisApiKeyStore = redisApiKeyStore;
    }

    public Mono<Boolean> isAllowed(String apiKey) {
        String bucketKey = "rateLimit:apikey:" + apiKey;
        System.out.println("Check for key: " + bucketKey);

        return getLimitStatus(apiKey)
                .doOnNext(status -> System.out.println("Key: " + bucketKey + " status: " + status))
                .flatMap(rateLimiter -> setLimitConfigFor(bucketKey, rateLimiter))
                .doOnNext(bucketProxy -> System.out.println("Key: " + bucketKey + " proxy: " + bucketProxy))
                .switchIfEmpty(setLimitConfigFor(bucketKey, new RateLimitStatus(noOfTokens, capacity, durationInMinutes) ))
                .doOnNext(bucketProxy -> System.out.println("Key: " + bucketKey + " proxy2: " + bucketProxy.getAvailableTokens()))
                .flatMap(bucket -> Mono.just(bucket.tryConsume(noOfTokens)));
    }

    public Mono<RateLimitStatus> getStatus(String apiKey) {
        String bucketKey = "rateLimit:apikey:" + apiKey;

        return getLimitStatus(apiKey)
                .flatMap(rateLimitStatus -> getLimitConfigFor(bucketKey)
                                .map(bucket -> {
                                    long remaining = bucket.getAvailableTokens();
                                    long capacity = rateLimitStatus.capacity();
                                    long refillNanos = Duration.ofMinutes(rateLimitStatus.nanosToRefill()).toNanos();
                                    return new RateLimitStatus(remaining, capacity, refillNanos);
                                })
                        );

    }

    private Mono<BucketProxy> setLimitConfigFor(String bucketKey, RateLimitStatus limitStatus) {
        return Mono.fromCallable(() -> proxyManager.builder()
                        .build(bucketKey.getBytes(), () -> BucketConfiguration.builder()
                                // 100 tokens max
                                // Refill 20 every 10 seconds
                                // Better UX than fixed window
                                .addLimit(burst(limitStatus.capacity(), 20, Duration.ofSeconds(10)))
                                .build()));
    }

    private Mono<BucketProxy> getLimitConfigFor(String bucketKey) {
        return Mono.fromCallable(() -> proxyManager.builder()
                        .build(bucketKey.getBytes(), () -> getLimitConfig(bucketKey)));
    }

    private BucketConfiguration getLimitConfig(String apiKey) {
        return proxyManager
                .getProxyConfiguration(apiKey.getBytes())
                .orElse(BucketConfiguration.builder().build());
    }


//    fixed window
    private Bandwidth simple(long capacity, Duration refillPeriod) {
        return Bandwidth.builder()
                .capacity(capacity)
                .refillIntervally(capacity, refillPeriod)
                .build();
    }

    // Burst + Refill Configuration
    private Bandwidth burst(long capacity, long refill, Duration refillPeriodInSeconds) {
        return Bandwidth.builder()
                .capacity(capacity)
                .refillGreedy(refill, refillPeriodInSeconds)
                .build();
    }

    private Mono<RateLimitStatus> getLimitStatus(String apiKey) {
        return redisApiKeyStore.validateClient(apiKey)
                .flatMap(this::checkForLimitStatus);
    }

    private Mono<RateLimitStatus> checkForLimitStatus(ApiClient apiClient) {
        System.out.println("Check for empty key: " + apiClient.rateLimitStatus());
        return Mono.justOrEmpty(apiClient.rateLimitStatus());
    }
}
