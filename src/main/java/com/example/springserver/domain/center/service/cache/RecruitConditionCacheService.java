package com.example.springserver.domain.center.service.cache;

import com.example.springserver.domain.center.cache.RecruitConditionCache;
import com.example.springserver.domain.center.cache.RecruitConditionCacheRepository;
import com.example.springserver.global.apiPayload.format.CacheException;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitConditionCacheService {

    private final RecruitConditionCacheRepository recruitConditionCacheRepository;
    private final Cache<Long, RecruitConditionCache> recruitConditionLocalCache;

    public void save(RecruitConditionCache cache) {
        recruitConditionCacheRepository.save(cache); // Redis 캐시
        recruitConditionLocalCache.put(cache.getId(), cache); // Local 캐시
    }

    public void saveAll(List<RecruitConditionCache> caches) {
        caches.forEach(this::save);
    }

    public RecruitConditionCache getByRecruitConditionId(Long recruitConditionId) {
        // 로컬 캐시 조회
        RecruitConditionCache cache = recruitConditionLocalCache.getIfPresent(recruitConditionId);
        if(cache != null) {
            log.info("[CAFFEINE] recruitCondition 로컬 캐시 조회 ======== ");
            return cache;
        }
        // Redis 캐시 조회
        return recruitConditionCacheRepository.findByRecruitConditionId(recruitConditionId)
                .map(redisCache -> {
                    log.info("[REDIS] recruitCondition 캐시 조회 ======== ");
                    recruitConditionLocalCache.put(recruitConditionId, redisCache); // Redis -> Local put
                    return redisCache;
                })
                .orElseThrow(() -> new CacheException(ErrorCode.RECRUIT_CONDITION_CACHE_MISS));
    }

    public List<RecruitConditionCache> getByElderIdFromRedis(Long elderId) {
        return recruitConditionCacheRepository.findByElderId(elderId);
    }

    public void deleteByRecruitConditionId(Long recruitConditionId) {
        recruitConditionCacheRepository.deleteByRecruitConditionId(recruitConditionId); // Redis 캐시 삭제
        recruitConditionLocalCache.invalidate(recruitConditionId); // Local 캐시 삭제
    }
}
