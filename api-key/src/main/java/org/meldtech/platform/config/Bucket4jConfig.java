package org.meldtech.platform.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Bucket4jConfig {
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;


    @Bean
    public RedisClient redisClient() {
        return RedisClient.create("redis://%s:%s".formatted(redisHost,redisPort));
    }

//    Or
//    @Bean
//    public RedisClient redisClient() {
//        return RedisClient.create(RedisURI.builder()
//                        .withHost(redisHost)
//                        .withPort(redisPort)
//                        .withSsl(false)
//                .build());
//    }

    @Bean
    public StatefulRedisConnection<byte[], byte[]> statefulRedisConnection(RedisClient client) {
        return client.connect(new io.lettuce.core.codec.ByteArrayCodec());
    }

    @Bean
    public LettuceBasedProxyManager<byte[]> bucket4jProxyManager(StatefulRedisConnection<byte[], byte[]> connection) {
        return LettuceBasedProxyManager.builderFor(connection)
                .build();
    }

//    Or

//    @Bean
//    public ProxyManager<String> bucket4jProxyManager() {
//        StatefulRedisConnection<String, byte[]> redisConnection = redisClient()
//                .connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));
//        return LettuceBasedProxyManager.builderFor(redisConnection)
//                .withExpirationStrategy(
//                        ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofMinutes(1L)))
//                .build();
//    }

//    @Bean
//    public Supplier<BucketConfiguration> bucketConfiguration() {
//        return () -> BucketConfiguration.builder()
//                .addLimit(Bandwidth.simple(200L, Duration.ofMinutes(1L)))
//                .build();
//    }
}
