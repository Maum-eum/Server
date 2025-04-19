package com.example.springserver.domain.center.cache;

import com.example.springserver.global.cache.RedisCacheHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class RecruitConditionCacheRepository {

    private final RedisCacheHelper cacheHelper;
    private static final String RECRUIT_PREFIX = "recruit_condition:";
    private static final String ELDER_PREFIX = "elder:";
    private static final Duration TTL = Duration.ofHours(6);

    private String getRecruitConditionKey(Long id) {
        return RECRUIT_PREFIX + id;
    }

    private String getElderSetKey(Long elderId) {
        return ELDER_PREFIX + elderId + ":recruitConditionIds";
    }

    public void save(RecruitConditionCache cache) {
        String mainKey = getRecruitConditionKey(cache.getId());
        String setKey = getElderSetKey(cache.getElderId());

        // 기존 Set에서 제거 후 다시 저장
        cacheHelper.removeFromSet(setKey, cache.getId().toString());
        cacheHelper.saveValue(mainKey, cache, TTL);
        cacheHelper.addToSet(setKey, cache.getId().toString());
    }

    public Optional<RecruitConditionCache> findByRecruitConditionId(Long id) {
        String key = getRecruitConditionKey(id);
        RecruitConditionCache value = cacheHelper.getValue(key, RecruitConditionCache.class);
        return Optional.ofNullable(value);
    }

    public List<RecruitConditionCache> findByElderId(Long elderId) {
        String setKey = getElderSetKey(elderId);
        Set<String> ids = cacheHelper.getSet(setKey);

        return ids.stream()
                .map(Long::parseLong)
                .map(this::findByRecruitConditionId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public void deleteByRecruitConditionId(Long id) {
        findByRecruitConditionId(id).ifPresent(cache -> {
            String setKey = getElderSetKey(cache.getElderId());
            cacheHelper.removeFromSet(setKey, id.toString());
        });

        cacheHelper.delete(getRecruitConditionKey(id));
    }

    public void deleteAllByElderId(Long elderId) {
        String setKey = getElderSetKey(elderId);
        Set<String> ids = cacheHelper.getSet(setKey);

        for (String id : ids) {
            cacheHelper.delete(getRecruitConditionKey(Long.parseLong(id)));
        }

        cacheHelper.deleteSet(setKey);
    }
}
