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

    public Optional<JobConditionCache> findByJobConditionId(Long jobConditionId) {
        String key = getJobConditionKey(jobConditionId);
        JobConditionCache result = (JobConditionCache) redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(result);
    }

    public Optional<JobConditionCache> findByCaregiverId(Long caregiverId) {
        Long jobConditionId = getJobConditionIdFromCache(caregiverId);

        if (jobConditionId == null) {
            log.warn("[CacheRepository] jobConditionId not exist - caregiverId: {}", caregiverId);
            return Optional.empty();
        }

        return findByJobConditionId(jobConditionId);
    }

    public void deleteByCaregiverId(Long caregiverId) {
        redisTemplate.delete(getCaregiverKey(caregiverId));
    }

    private Long getJobConditionIdFromCache(Long caregiverKey) {
        Object value = redisTemplate.opsForValue().get(getCaregiverKey(caregiverKey));

        if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof Integer) {
            return ((Integer) value).longValue();  // Integer → Long 변환
        } else if (value instanceof String) {
            try {
                return Long.parseLong((String) value); // 문자열로 저장된 경우
            } catch (NumberFormatException e) {
                log.warn("[CacheRepository] Cannot parse jobConditionId from string: {}", value);
            }
        }
        log.warn("[CacheRepository] Expected Long but got: {} type", value != null ? value.getClass() : "null");
        return null;
    }
}