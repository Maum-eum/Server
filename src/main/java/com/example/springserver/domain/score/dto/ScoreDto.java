package com.example.springserver.domain.score.dto;

import com.example.springserver.domain.match.entity.enums.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
public class ScoreDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RecommendCaregiverListDto{
        List<RecommendCaregiverDto> recommendCaregivers;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RecommendCaregiverDto {
        Long jobConditonId;
        Integer score;
        String imgUrl;
        String caregiverName;
        MatchStatus matchStatus;
    }
}
