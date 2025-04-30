package com.example.springserver.global.cache.config;

import com.example.springserver.global.cache.type.LocalCacheType;
import com.example.springserver.global.cache.type.RedisCacheType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;

@Configuration
@EnableCaching
public class CacheConfig {
    private ObjectMapper objectMapper() {
        PolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType(Object.class)
                .build();

        return new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(new JavaTimeModule())
                .activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.NON_FINAL);
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // 1. Caffeine CacheManager
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager() {
            @Override
            protected com.github.benmanes.caffeine.cache.Cache<Object, Object> createNativeCaffeineCache(String name) {
                Optional<LocalCacheType> matched = Arrays.stream(LocalCacheType.values())
                        .filter(type -> type.getCacheName().equals(name))
                        .findFirst();

                if (matched.isPresent()) {
                    LocalCacheType type = matched.get();
                    return Caffeine.newBuilder()
                            .expireAfterWrite(type.getExpiredAfterWrite(), TimeUnit.MINUTES)
                            .maximumSize(type.getMaximumSize())
                            .build();
                }

                // 기본 설정
                return Caffeine.newBuilder()
                        .expireAfterWrite(2, TimeUnit.MINUTES)
                        .maximumSize(1000)
                        .build();
            }
        };

        // 2. RedisCacheManager
        RedisCacheManager redisCacheManager = RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultCacheConfiguration())
                .withInitialCacheConfigurations(getCustomCacheConfigurations())
                .build();

        // 3. CompositeCacheManager
        CompositeCacheManager compositeCacheManager = new CompositeCacheManager(caffeineCacheManager, redisCacheManager);
        compositeCacheManager.setFallbackToNoOpCache(false);
        return compositeCacheManager;
    }

    private Map<String, RedisCacheConfiguration> getCustomCacheConfigurations() {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        for (RedisCacheType type : RedisCacheType.values()) {
            cacheConfigurations.put(
                    type.getCacheName(),
                    RedisCacheConfiguration.defaultCacheConfig()
                            .serializeValuesWith(fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper())))
                            .entryTtl(type.getTtl())
            );
        }

        return cacheConfigurations;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    private RedisCacheConfiguration defaultCacheConfiguration() {
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .disableCachingNullValues()
                .serializeKeysWith(fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper())))
                .entryTtl(Duration.ofMinutes(2));
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        // 직렬화
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        redisTemplate.setDefaultSerializer(serializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}