package com.example.springserver.domain.caregiver.cache;

import com.example.springserver.global.cache.RedisCacheHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JobConditionCacheRepository {

    private final RedisCacheHelper cacheHelper;

    private static final String JOB_CONDITION_KEY_PREFIX = "job_condition:";
    private static final String CAREGIVER_KEY_PREFIX = "care_giver:";
    private static final Duration TTL = Duration.ofHours(6);

    private String getJobConditionKey(Long id) {
        return JOB_CONDITION_KEY_PREFIX + id;
    }

    private String getCaregiverKey(Long caregiverId) {
        return CAREGIVER_KEY_PREFIX + caregiverId;
    }

    public void save(JobConditionCache cache) {
        String jobKey = getJobConditionKey(cache.getId());
        String caregiverKey = getCaregiverKey(cache.getCaregiverId());

        cacheHelper.saveValue(jobKey, cache, TTL);
        cacheHelper.saveValue(caregiverKey, cache.getId(), TTL); // caregiverId → jobConditionId 매핑
    }

    public Optional<JobConditionCache> findByJobConditionId(Long id) {
        return cacheHelper.get(getJobConditionKey(id), JobConditionCache.class);
    }

    public Optional<JobConditionCache> findByCaregiverId(Long caregiverId) {
        return cacheHelper.get(getCaregiverKey(caregiverId), Long.class)
                .flatMap(this::findByJobConditionId);
    }

    public void deleteByCaregiverId(Long caregiverId) {
        String caregiverKey = getCaregiverKey(caregiverId);

        cacheHelper.get(caregiverKey, Long.class)
                .ifPresent(jobConditionId -> cacheHelper.delete(getJobConditionKey(jobConditionId)));

        cacheHelper.delete(caregiverKey);
    }
}
