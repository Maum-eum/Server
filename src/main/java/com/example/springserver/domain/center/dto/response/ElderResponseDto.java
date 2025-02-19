package com.example.springserver.domain.center.dto.response;

import com.example.springserver.domain.center.entity.enums.ElderRate;
import com.example.springserver.domain.center.entity.enums.Inmate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class ElderResponseDto {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDto {
        private Long elderId;
        private String name;
        private int gender;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseDto {
        private Long elderId;
        private String name;
        private String centerName;
        private int gender;
        private LocalDate birth;
        private List<Inmate> inmateTypes;
        private ElderRate rate;
        private String img;
        private Integer weight;
        private Long careId;
        private boolean isTemporarySave; // 임시 저장 여부
        private boolean isNormal; // 증상 보유 여부
        private boolean hasShortTermMemoryLoss; // 단기 기억 장애 여부
        private boolean wandersOutside; // 집밖을 배회 여부
        private boolean actsLikeChild; // 어린아이 같은 행동 여부
        private boolean hasDelusions; // 망상 여부
        private boolean hasAggressiveBehavior; // 공격적인 행동 여부
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateResponseDto {
        private Long elderId;
        private String name;
        private String centerName;
        private ElderRate rate;
        private List<Inmate> inmateTypes;
        private String img;
        private Integer weight;
        private boolean isTemporarySave; // 임시 저장 여부
        private boolean isNormal; // 증상 보유 여부
        private boolean hasShortTermMemoryLoss; // 단기 기억 장애 여부
        private boolean wandersOutside; // 집밖을 배회 여부
        private boolean actsLikeChild; // 어린아이 같은 행동 여부
        private boolean hasDelusions; // 망상 여부
        private boolean hasAggressiveBehavior; // 공격적인 행동 여부
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteResponseDto {
        private Long elderId;
        private String name;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MatchElderResponseDto {
        private Long elderId;
        private String name;
        private String imgUrl;
    }

}