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
    private static final Duration TTL = Duration.ofHours(6);

    private String getJobConditionKey(Long id) {
        return JOB_CONDITION_KEY_PREFIX + id;
    }

    public void save(JobConditionCache cache) {
        String jobKey = getJobConditionKey(cache.getId());
        cacheHelper.saveValue(jobKey, cache, TTL);
    }

    public Optional<JobConditionCache> findByJobConditionId(Long id) {
        return cacheHelper.get(getJobConditionKey(id), JobConditionCache.class);
    }

    public void deleteByCaregiverId(Long id) {
        String caregiverKey = getJobConditionKey(id);

        cacheHelper.get(caregiverKey, Long.class)
                .ifPresent(jobConditionId -> cacheHelper.delete(getJobConditionKey(jobConditionId)));

        cacheHelper.delete(caregiverKey);
    }
}
