package org.meldtech.platform.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

//This limits each key to 100 requests per minute.
@Service
public class RateLimiterService {
    private final ProxyManager<byte[]> proxyManager;

    @Value("${rate.limit.capacity}")
    private long capacity;

    @Value("${rate.limit.durationInMinutes}")
    private long durationInMinutes;

    @Value("${rate.limit.noOfTokens}")
    private long noOfTokens;

    public RateLimiterService(ProxyManager<byte[]> proxyManager) {
        this.proxyManager = proxyManager;
    }

    public Mono<Boolean> isAllowed(String apiKey) {
        String bucketKey = "rateLimit:apikey:" + apiKey;

        return Mono.fromCallable(() -> proxyManager.builder()
                        .build(bucketKey.getBytes(), () -> BucketConfiguration.builder()
                                .addLimit(simple(capacity, Duration.ofMinutes(durationInMinutes)))
                                .build()))
                .flatMap(bucket -> Mono.just(bucket.tryConsume(noOfTokens)));
    }

    private Bandwidth simple(long capacity, Duration refillPeriod) {
        return Bandwidth.builder()
                .capacity(capacity)
                .refillIntervally(capacity, refillPeriod)
                .build();
    }
}
