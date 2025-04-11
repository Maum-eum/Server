package com.example.springserver.domain.caregiver.repository;

import com.example.springserver.global.cache.model.JobConditionCache;
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

    private static final String CACHE_KEY_PREFIX = "job_condition:";
    private static final Duration TTL = Duration.ofHours(6); // 캐시 유효 시간 설정

    private String getKey(Long jobConditionId) {
        return CACHE_KEY_PREFIX + jobConditionId;
    }

    private String getCaregiverKey(Long caregiverId) {
        return CACHE_KEY_PREFIX + "caregiver:" + caregiverId;
    }

    public void save(JobConditionCache jobConditionCache) {
        String mainKey = getKey(jobConditionCache.getId());
        String caregiverKey = getCaregiverKey(jobConditionCache.getCaregiverId());

        redisTemplate.opsForValue().set(mainKey, jobConditionCache, TTL);
        redisTemplate.opsForValue().set(caregiverKey, jobConditionCache.getId(), TTL);
    }

    public Optional<JobConditionCache> findById(Long jobConditionId) {
        String key = getKey(jobConditionId);
        JobConditionCache result = (JobConditionCache) redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(result);
    }

    public Optional<JobConditionCache> findByCaregiverId(Long caregiverId) {
        Long jobConditionId = getJobConditionIdFromCache(caregiverId);
        if (jobConditionId == null) return Optional.empty();

        return findById(jobConditionId);
    }

    public void deleteByCaregiverId(Long caregiverId) {
        Long jobConditionId = getJobConditionIdFromCache(caregiverId);
        if (jobConditionId != null) {
            redisTemplate.delete(getKey(jobConditionId));
        }
        redisTemplate.delete(getCaregiverKey(caregiverId));
    }

    private Long getJobConditionIdFromCache(Long caregiverId) {
        Object value = redisTemplate.opsForValue().get(getCaregiverKey(caregiverId));
        if (value instanceof Long) {
            return (Long) value;
        } else {
            log.warn("Expected Long but got: {}", value != null ? value.getClass() : "null");
            return null;
        }
    }
}