package com.example.springserver.domain.score.service.cache;

import com.example.springserver.domain.score.cache.MatchScoreCache;
import com.example.springserver.domain.score.cache.MatchScoreCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchScoreCacheService {

    private final MatchScoreCacheRepository cacheRepository;

    public List<MatchScoreCache> getByRecruitConditionId(Long recruitConditionId) {
        return cacheRepository.findByRecruitConditionId(recruitConditionId);
    }

    public void saveAll(List<MatchScoreCache> caches) {
        cacheRepository.saveAll(caches);
    }
}
