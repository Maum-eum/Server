package com.example.springserver.domain.caregiver.service.cache;

import com.example.springserver.domain.caregiver.cache.JobConditionCache;
import com.example.springserver.domain.caregiver.cache.JobConditionCacheRepository;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class JobConditionCacheService {

    private final JobConditionCacheRepository jobConditionCacheRepository;

    public void save(JobConditionCache cache) {
        jobConditionCacheRepository.save(cache);
    }

    public JobConditionCache getByCaregiverKey(Long caregiverKey) {
        return jobConditionCacheRepository.findByCaregiverKey(caregiverKey)
                .orElseThrow(() -> new GlobalException(ErrorCode.JOB_CONDITION_NOT_FOUND));
    }

    public void deleteByCaregiverKey(Long caregiverKey) {
        jobConditionCacheRepository.deleteByCaregiverKey(caregiverKey);
    }
}
