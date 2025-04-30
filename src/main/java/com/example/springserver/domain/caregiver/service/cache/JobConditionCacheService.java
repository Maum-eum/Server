package com.example.springserver.domain.caregiver.service.cache;

import com.example.springserver.domain.caregiver.cache.JobConditionCache;
import com.example.springserver.domain.caregiver.cache.JobConditionCacheRepository;
import com.example.springserver.global.apiPayload.format.CacheException;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobConditionCacheService {

    private final JobConditionCacheRepository jobConditionCacheRepository;
    private final Cache<Long, JobConditionCache> jobConditionLocalCache;

    public void save(JobConditionCache cache) {
        jobConditionCacheRepository.save(cache); // Redis 캐시
        jobConditionLocalCache.put(cache.getCaregiverId(), cache); // Local 캐시
    }

    public JobConditionCache getByCaregiverKey(Long caregiverKey) {
        // 로컬 캐시 확인
        JobConditionCache cache = jobConditionLocalCache.getIfPresent(caregiverKey);
        if (cache != null) {
            log.info("[CAFFEINE] jobCondition 로컬 캐시 조회 ======== ");
            return cache;
        }

        // Redis 캐시 확인
        return jobConditionCacheRepository.findByCaregiverId(caregiverKey)
                .map(redisCache -> {
                    log.info("[REDIS] jobCondition 캐시 조회 ======== ");
                    jobConditionLocalCache.put(caregiverKey, redisCache); // Redis -> Local put
                    return redisCache;
                })
                .orElseThrow(() -> new CacheException(ErrorCode.JOB_CONDITION_CACHE_MISS));
    }

    public void deleteByCaregiverKey(Long caregiverKey) {
        jobConditionCacheRepository.deleteByCaregiverId(caregiverKey); // Redis 캐시 삭제
        jobConditionLocalCache.invalidate(caregiverKey); // Local 캐시 삭제
    }
}

