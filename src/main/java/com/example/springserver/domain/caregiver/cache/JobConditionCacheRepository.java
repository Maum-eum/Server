package com.example.springserver.domain.caregiver.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.time.Duration;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JobConditionCacheRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String JOB_CONDITION_CACHE_KEY_PREFIX = "job_condition:";
    private static final String CARE_GIVER_CACHE_KEY_PREFIX = "care_giver:";
    private static final Duration TTL = Duration.ofHours(6); // 캐시 유효 시간 설정

    private String getJobConditionKey(Long jobConditionId) {
        return JOB_CONDITION_CACHE_KEY_PREFIX + jobConditionId;
    }

    private String getCaregiverKey(Long caregiverId) {
        return CARE_GIVER_CACHE_KEY_PREFIX + caregiverId;
    }

    public void save(JobConditionCache jobConditionCache) {
        String mainKey = getJobConditionKey(jobConditionCache.getId());
        String subKey = getCaregiverKey(jobConditionCache.getCaregiverId());

        redisTemplate.opsForValue().set(mainKey, jobConditionCache, TTL);
        redisTemplate.opsForValue().set(subKey, jobConditionCache.getId(), TTL);
    }

    public Optional<JobConditionCache> findByJobConditionKey(Long jobConditionKey) {
        String key = getJobConditionKey(jobConditionKey);
        JobConditionCache result = (JobConditionCache) redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(result);
    }

    public Optional<JobConditionCache> findByCaregiverKey(Long caregiverKey) {
        Long jobConditionId = getJobConditionIdFromCache(caregiverKey);
        if (jobConditionId == null) return Optional.empty();

        return findByJobConditionKey(jobConditionId);
    }

    public void deleteByCaregiverKey(Long caregiverKey) {
        Long jobConditionKey = getJobConditionIdFromCache(caregiverKey);
        if (jobConditionKey != null) {
            redisTemplate.delete(getJobConditionKey(jobConditionKey));
        }
        redisTemplate.delete(getCaregiverKey(caregiverKey));
    }

    private Long getJobConditionIdFromCache(Long caregiverKey) {
        Object value = redisTemplate.opsForValue().get(getCaregiverKey(caregiverKey));
        if (value instanceof Long) {
            return (Long) value;
        } else {
            log.warn("Expected Long but got: {}", value != null ? value.getClass() : "null");
            return null;
        }
    }
}