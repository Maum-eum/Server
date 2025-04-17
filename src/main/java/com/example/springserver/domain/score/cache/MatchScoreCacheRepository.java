package com.example.springserver.domain.score.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MatchScoreCacheRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String KEY_PREFIX = "match_score:";

    public List<MatchScoreCache> findByRecruitConditionId(Long recruitConditionId) {
        String pattern = KEY_PREFIX + recruitConditionId + ":";
        Cursor<byte[]> cursor = redisTemplate.execute((RedisCallback<Cursor<byte[]>>) connection ->
                connection.scan(ScanOptions.scanOptions().match(pattern + "*").build()));

        List<MatchScoreCache> result = new ArrayList<>();
        while (cursor != null && cursor.hasNext()) {
            byte[] key = cursor.next();
            Object cache = redisTemplate.opsForValue().get(new String(key));
            if (cache != null) {
                result.add((MatchScoreCache) cache);
            }
        }
        return result;
    }

    public void saveAll(List<MatchScoreCache> caches) {
        // Redis 파이프라인 처리
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (MatchScoreCache cache : caches) {
                String key = KEY_PREFIX + cache.getRecruitConditionId() + ":" + cache.getId();
                connection.set(key.getBytes(), serialize(cache));
            }
            return null;
        });
    }

    private byte[] serialize(MatchScoreCache cache) {
        try {
            return new ObjectMapper().writeValueAsBytes(cache);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize cache", e);
        }
    }
}
