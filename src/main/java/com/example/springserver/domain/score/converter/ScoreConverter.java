package com.example.springserver.domain.score.converter;

import com.example.springserver.domain.score.dto.ScoreDto.*;
import com.example.springserver.domain.score.entity.MatchScore;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ScoreConverter {

    public static RecommendCaregiverListDto toRecommendCaregiverListDto(List<MatchScore> scores){
        List<RecommendCaregiverDto> recommendCaregivers = scores.stream()
                .map(ScoreConverter::toRecommendCaregiverDto)
                .toList();

        return RecommendCaregiverListDto.builder()
                .recommendCaregivers(recommendCaregivers)
                .build();
    }

    public static RecommendCaregiverDto toRecommendCaregiverDto(MatchScore score){
        return RecommendCaregiverDto.builder()
                .jobConditonId(score.getJobCondition().getId())
                .score(score.getScore())
                .imgUrl(score.getCaregiverImg())
                .caregiverName(score.getCaregiverName())
                .matchStatus(score.getStatus())
                .build();
    }
}
