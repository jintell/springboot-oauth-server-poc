package org.meldtech.platform.service;

import jakarta.validation.constraints.NotNull;
import org.meldtech.platform.storage.RedisApiKeyStore;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

// Track Usage Per API Key (Time Series)
@Service
public class UsageTrackerService {
    private final ReactiveStringRedisTemplate redisTemplate;
    private final RedisApiKeyStore redisApiKeyStore;

    public UsageTrackerService(ReactiveStringRedisTemplate redisTemplate,
                               RedisApiKeyStore redisApiKeyStore) {
        this.redisTemplate = redisTemplate;
        this.redisApiKeyStore = redisApiKeyStore;
    }

    public Mono<Void> recordUsage(String apiKey) {
        String timestampKey = getCurrentMinuteKey(apiKey);
        return redisTemplate.opsForValue().increment(timestampKey)
                .then(redisTemplate.expire(timestampKey, java.time.Duration.ofDays(1)))
                .then();
    }

    public Flux<Map<String, Object>> getUsage(String apiKey, int minutesBack) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

        List<String> keys = new ArrayList<>();
        for (int i = 0; i < minutesBack; i++) {
            String timeKey = now.minusMinutes(i).format(formatter);
            keys.add(String.format("usage:apikey:%s:%s", apiKey, timeKey));
        }

        return Flux.fromIterable(keys)
                .flatMap(key ->
                        redisTemplate.opsForValue().get(key)
                                .map(val -> {
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("timestamp", key.split(":")[3]);
                                    data.put("count", Integer.parseInt(val));
                                    return data;
                                })
                );
    }

    public Flux<Map<String, Object>> getAllKeySummaries() {
        return redisTemplate.keys("usage:apikey:*")
                .flatMap(key ->
                        redisTemplate.opsForValue().get(key)
                                .map(val -> {
                                    String[] parts = key.split(":");
                                    String apiKey = parts[2];
                                    String minute = parts[3];
                                    return Map.of("key", apiKey, "timestamp", minute, "count", Integer.parseInt(val));
                                })
                )
                .collectMultimap(m -> m.get("key"))
                .flatMapMany(grouped -> Flux.fromIterable(grouped.entrySet()))
                .map(entry -> {
                    String key = entry.getKey().toString();
                    List<Map<String, Object>> vals = getValue(entry.getValue());
                    int total = vals.stream().mapToInt(v -> (int) v.get("count")).sum();
                    // Optional: Fetch from RedisApiKeyStore
                    return Map.of("key", key, "total", total);
                }).flatMap(this::getOwner);
    }

    private String getCurrentMinuteKey(String apiKey) {
        String timeBucket = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        return String.format("usage:apikey:%s:%s", apiKey, timeBucket);
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getValue(@NotNull Object val) {
        return (List<Map<String, Object>>) val;
    }

    private Mono<Map<String, Object>> getOwner(Map<String, ? extends Serializable> data) {
        String apiKey = data.get("key").toString();
        int total = (int) data.get("total");
        return redisApiKeyStore.getOwner(apiKey)
                .map(owner -> Map.of("key", apiKey, "total", total, "owner", owner));
    }
}
