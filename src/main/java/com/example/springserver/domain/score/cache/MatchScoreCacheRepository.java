package com.example.springserver.domain.score.cache;

import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class MatchScoreCacheRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String KEY_PREFIX = "match_score:";

    public String getKeyByRecruitConditionId(Long id) {return KEY_PREFIX + id;}

    public List<MatchScoreCache> findTopByRecruitConditionId(Long recruitConditionId) {
        String key = getKeyByRecruitConditionId(recruitConditionId);
        Set<Object> results = redisTemplate.opsForZSet().reverseRange(key, 0, -1);

        if (results == null) return List.of();

        return results.stream()
                .map(this::deserialize)
                .toList();
    }

    public void saveAll(List<MatchScoreCache> caches) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (MatchScoreCache cache : caches) {
                String key = getKeyByRecruitConditionId(cache.getRecruitConditionId());
                String value = serialize(cache);
                double score = cache.getScore();

                redisTemplate.opsForZSet().add(key, value, score);
                redisTemplate.expire(key, Duration.ofMinutes(60)); // 임시 1시간 설정
            }
            return null;
        });
    }

    // 직렬화
    private String serialize(MatchScoreCache cache) {
        try {
            return objectMapper.writeValueAsString(cache);
        } catch (JsonProcessingException e) {
            throw new GlobalException(ErrorCode.MATCH_SCORE_SERIALIZE_FAILED);
        }
    }

    // 역직렬화
    private MatchScoreCache deserialize(Object value) {
        if (value instanceof String stringValue) {
            try {
                return objectMapper.readValue(stringValue, MatchScoreCache.class);
            } catch (JsonProcessingException e) {
                throw new GlobalException(ErrorCode.MATCH_SCORE_DESERIALIZE_FAILED);
            }
        } else {
            throw new GlobalException(ErrorCode.MATCH_SCORE_DESERIALIZE_FAILED);
        }
    }
}
