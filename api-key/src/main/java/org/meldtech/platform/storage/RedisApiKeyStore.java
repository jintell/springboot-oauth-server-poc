package org.meldtech.platform.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.meldtech.platform.model.ApiClient;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Component
public class RedisApiKeyStore {
    private final ReactiveStringRedisTemplate redisTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    private static final String API_KEY_PREFIX = "apikey:";

    public RedisApiKeyStore(ReactiveStringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Mono<Boolean> isValid(String apiKey) {
        return redisTemplate.hasKey(setApiKey(apiKey));
    }

    public Mono<ApiClient> validateClient(String apiKey) {
        return isValid(apiKey)
                .flatMap(valid -> valid ? getValue(apiKey) : Mono.empty());
    }

    public Mono<String> getOwner(String apiKey) {
        return redisTemplate.opsForValue()
                .get(setApiKey(apiKey))
                .mapNotNull(s -> convent(s, ApiClient.class))
                .map(ApiClient::clientName);
    }

    // Optional: Admin method to add keys
    public Mono<Boolean> addKey(ApiClient apiClient) {
        return redisTemplate.opsForValue()
                .set(setApiKey(apiClient.clientApiKey()), Objects.requireNonNull(convent(apiClient, String.class)))
                .thenReturn(true);
    }

    public Mono<Boolean> deleteKey(String apiKey) {
        return redisTemplate.delete(setApiKey(apiKey))
                .map(deleted -> deleted > 0);
    }

    public Mono<List<ApiClient>> getKey() {
        return redisTemplate.keys(setApiKey("*").replace(":", ""))
                .flatMap(this::getValueWithPrefix)
                        .doOnNext(s -> System.out.println("key-value: " + s))
                .collectList();
    }

    private Mono<ApiClient> getValue(String key) {
        return redisTemplate.opsForValue()
                .get(setApiKey(key))
                .mapNotNull(value -> convent(value, ApiClient.class));
    }

    private Mono<ApiClient> getValueWithPrefix(String key) {
        return redisTemplate.opsForValue()
                .get(key)
                .mapNotNull(value -> convent(value, ApiClient.class));
    }

    private String setApiKey(String key) {
        return  API_KEY_PREFIX.concat(key);
    }


    private <T> T convent(Object data, Class<T> clazz) {
        try {
            if(data instanceof String d) return mapper.readValue(d, clazz);
            return clazz.cast(mapper.writeValueAsString(data));
        } catch (Exception e) {
            return null;
        }
    }

}
