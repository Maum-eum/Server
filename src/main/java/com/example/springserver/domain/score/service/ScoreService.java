package com.example.springserver.domain.score.service;

import com.example.springserver.domain.score.converter.ScoreConverter;
import com.example.springserver.domain.score.dto.ScoreDto;
import com.example.springserver.domain.score.entity.MatchScore;
import com.example.springserver.domain.score.repository.ScoreRepository;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScoreService {

    private final ScoreRepository scoreRepository;

    public ScoreDto.RecommendCaregiverListDto getRecommendList(Long request) {
        List<MatchScore> byRecruitConditionId = scoreRepository.findByRecruitConditionId(request)
                .orElseThrow(()-> new GlobalException(ErrorCode.RECOMMEND_LIST_NOT_FOUND));
        return ScoreConverter.toRecommendCaregiverListDto(byRecruitConditionId);
    }
}
