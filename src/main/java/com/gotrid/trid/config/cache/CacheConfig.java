package com.gotrid.trid.config.cache;

import com.gotrid.trid.cache.RedisWildcardEvictionAspect;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
@EnableAspectJAutoProxy
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        //* Define cache configurations with memory limits (but memory limits are not enforced by Redis)
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        // High-frequency, small objects (10 MB)
        cacheConfigurations.put("users", createCacheConfig(Duration.ofMinutes(15), 5_000));
        cacheConfigurations.put("shops", createCacheConfig(Duration.ofMinutes(30), 2_000));
        cacheConfigurations.put("userProfiles", createCacheConfig(Duration.ofMinutes(20), 3_000));

        // Medium-frequency, medium objects (15 MB)
        cacheConfigurations.put("allShops", createCacheConfig(Duration.ofMinutes(10), 1_000));
        cacheConfigurations.put("userCarts", createCacheConfig(Duration.ofMinutes(5), 1_500));
        cacheConfigurations.put("products", createCacheConfig(Duration.ofMinutes(20), 3_000));
        cacheConfigurations.put("productVariant", createCacheConfig(Duration.ofMinutes(20), 3_000));
        cacheConfigurations.put("productModel", createCacheConfig(Duration.ofMinutes(30), 2_000));

        // Low-frequency, large objects (5 MB)
        cacheConfigurations.put("shopModels", createCacheConfig(Duration.ofHours(1), 100));
        cacheConfigurations.put("productImages", createCacheConfig(Duration.ofHours(2), 200));
        cacheConfigurations.put("sellerOrders", createCacheConfig(Duration.ofMinutes(8), 800));


        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(15))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues(); // Don't cache null/empty results

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
//                .transactionAware() //* Optional: enables transaction support for cache operations
                .build();
    }

    private RedisCacheConfiguration createCacheConfig(Duration ttl, long maxEntries) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl)
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues()
                // Note: Redis doesn't have built-in max entries, but you can configure Redis max-memory
                .computePrefixWith(cacheName -> cacheName + "::");
    }


    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // Use String serializer for keys
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Use JSON serializer for values
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisWildcardEvictionAspect redisWildcardEvictionAspect(RedisTemplate<String, Object> redisTemplate) {
        return new RedisWildcardEvictionAspect(redisTemplate);
    }
}