package com.example.springserver.domain.center.service.cache;

import com.example.springserver.domain.center.cache.RecruitConditionCache;
import com.example.springserver.domain.center.cache.RecruitConditionCacheRepository;
import com.example.springserver.global.apiPayload.format.CacheException;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitConditionCacheService {

    private final RecruitConditionCacheRepository recruitConditionCacheRepository;

    public void save(RecruitConditionCache cache) {
        recruitConditionCacheRepository.save(cache);
    }

    public void saveAll(List<RecruitConditionCache> caches) {
        caches.forEach(this::save);
    }

    public RecruitConditionCache getByRecruitConditionId(Long recruitConditionId) {
        return recruitConditionCacheRepository.findByRecruitConditionId(recruitConditionId)
                .orElseThrow(() -> new CacheException(ErrorCode.RECRUIT_CONDITION_CACHE_MISS));
    }

    public List<RecruitConditionCache> getByElderIdFromRedis(Long elderId) {
        return recruitConditionCacheRepository.findByElderId(elderId);
    }

    public void deleteByRecruitConditionId(Long recruitConditionId) {
        recruitConditionCacheRepository.deleteByRecruitConditionId(recruitConditionId);
    }

    public void deleteAllByElderId(Long elderId) {
        recruitConditionCacheRepository.deleteAllByElderId(elderId);
    }
}
