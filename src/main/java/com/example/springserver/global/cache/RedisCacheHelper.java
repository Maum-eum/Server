package com.example.springserver.global.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RedisCacheHelper {

    private final RedisTemplate<String, Object> redisTemplate;

    // 저장
    public void saveValue(String key, Object value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    // 조회
    public <T> T getValue(String key, Class<T> type) {
        Object value = redisTemplate.opsForValue().get(key);
        return type.cast(value); // 안전한 타입 반환
    }

    // 삭제
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    // Set 추가
    public void addToSet(String key, String value) {
        redisTemplate.opsForSet().add(key, value);
    }

    public <T> Optional<T> get(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) return Optional.empty();

        try {
            return Optional.of(clazz.cast(value));
        } catch (ClassCastException e) {
            return Optional.empty();
        }
    }

    // Set 조회
    public Set<String> getSet(String key) {
        Set<Object> rawSet = redisTemplate.opsForSet().members(key);
        if (rawSet == null) return Collections.emptySet();

        return rawSet.stream()
                .map(Object::toString)
                .collect(Collectors.toSet());
    }

    // Set 요소 제거
    public void removeFromSet(String key, String value) {
        redisTemplate.opsForSet().remove(key, value);
    }

    // Set 전체 삭제
    public void deleteSet(String key) {
        redisTemplate.delete(key);
    }
}