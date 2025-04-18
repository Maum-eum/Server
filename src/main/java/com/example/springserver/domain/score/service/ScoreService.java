package com.example.springserver.domain.score.service;

import com.example.springserver.domain.score.cache.MatchScoreCache;
import com.example.springserver.domain.score.cache.MatchScoreCacheConverter;
import com.example.springserver.domain.score.converter.ScoreConverter;
import com.example.springserver.domain.score.dto.ScoreDto;
import com.example.springserver.domain.score.entity.MatchScore;
import com.example.springserver.domain.score.repository.ScoreRepository;
import com.example.springserver.domain.score.service.cache.MatchScoreCacheService;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ScoreService {

    private final ScoreRepository scoreRepository;
    private final MatchScoreCacheService matchScoreCacheService;
    private final MatchScoreCacheConverter matchScoreCacheConverter;

    public ScoreDto.RecommendCaregiverListDto getRecommendList(Long recruitConditionId) {
        // Redis에서 캐시 조회
        List<MatchScoreCache> cachedScores = matchScoreCacheService.getByRecruitConditionId(recruitConditionId);

        // Cache hit
        if (!cachedScores.isEmpty()) {

            List<MatchScore> scores = cachedScores.stream()
                    .map(matchScoreCacheConverter::fromCache)
                    .toList();
            return ScoreConverter.toRecommendCaregiverListDto(scores);
        }

        // Cache miss → DB 조회
        List<MatchScore> scores = scoreRepository.findByRecruitConditionId(recruitConditionId)
                .orElseThrow(() -> new GlobalException(ErrorCode.RECOMMEND_LIST_NOT_FOUND));

        // DB data -> cache로 변환 & 저장
        List<MatchScoreCache> newCaches = scores.stream()
                .map(matchScoreCacheConverter::toCache)
                .toList();

        matchScoreCacheService.saveAll(newCaches);
        return ScoreConverter.toRecommendCaregiverListDto(scores);
    }
}
