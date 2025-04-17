package com.example.springserver.domain.center.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RecruitConditionCacheRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String RECRUIT_CONDITION_HASH_KEY_PREFIX = "recruit_condition:";
    private static final String ELDER_HASH_KEY_PREFIX = "elder:";
    private static final Duration TTL = Duration.ofHours(6);

    private String getRecruitConditionHashKey(Long recruitConditionId) {
        return RECRUIT_CONDITION_HASH_KEY_PREFIX + recruitConditionId;
    }

    private String getElderListHashKey(Long elderId) {
        return ELDER_HASH_KEY_PREFIX + elderId + ":recruitConditionIds";
    }

    public void save(RecruitConditionCache conditionCache) {
        String mainKey = getRecruitConditionHashKey(conditionCache.getId());
        String listKey = getElderListHashKey(conditionCache.getElderId());

        // 기존 값 삭제
        redisTemplate.opsForList().remove(listKey, 0, conditionCache.getId());
        // 저장
        redisTemplate.opsForValue().set(mainKey, conditionCache, TTL);
        redisTemplate.opsForList().rightPush(listKey, conditionCache.getId());
        redisTemplate.expire(listKey, TTL);
    }

    public Optional<RecruitConditionCache> findByRecruitConditionId(Long recruitConditionId) {
        String key = getRecruitConditionHashKey(recruitConditionId);
        RecruitConditionCache result = (RecruitConditionCache) redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(result);
    }

    public List<RecruitConditionCache> findByElderId(Long elderId) {
        String listKey = getElderListHashKey(elderId);
        List<Object> idList = redisTemplate.opsForList().range(listKey, 0, -1);

        return idList.stream()
                .map(id -> findByRecruitConditionId(Long.parseLong(id.toString())))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public void deleteByRecruitConditionId(Long recruitConditionId) {
        findByRecruitConditionId(recruitConditionId).ifPresent(cache -> {
            String listKey = getElderListHashKey(cache.getElderId());
            redisTemplate.opsForList().remove(listKey, 1, recruitConditionId);
        });

        redisTemplate.delete(getRecruitConditionHashKey(recruitConditionId));
    }

    public void deleteAllByElderId(Long elderId) {
        String listKey = getElderListHashKey(elderId);
        List<Object> idList = redisTemplate.opsForList().range(listKey, 0, -1);

        if (idList != null) {
            idList.forEach(id -> redisTemplate.delete(getRecruitConditionHashKey(Long.parseLong(id.toString()))));
        }

        redisTemplate.delete(listKey);
    }
}