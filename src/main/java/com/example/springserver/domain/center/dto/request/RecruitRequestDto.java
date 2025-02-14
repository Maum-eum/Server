package com.example.springserver.domain.center.dto.request;

import com.example.springserver.domain.center.entity.RecruitCondition;
import com.example.springserver.domain.center.entity.enums.CareType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

public class RecruitRequestDto {

    @Getter
    public static class CreateReqDto {

        private CareType careType; // 근무 종류

        private boolean flexibleSchedule; // 시간 협의 여부

        private int desiredHourlyWage; // 희망 급여

        private boolean selfFeeding; // 스스로 식사 가능

        private boolean mealPreparation; // 식사 차려드리기

        private boolean cookingAssistance; // 요리 필요

        private boolean enteralNutritionSupport; // 경관식 보조

        private boolean selfToileting; // 스스로 배변 가능

        private boolean occasionalToiletingAssist; // 가끔 대소변 실수

        private boolean diaperCare; // 기저귀 케어 필요

        private boolean catheterOrStomaCare; // 유치도뇨/방광루/장루 관리

        private boolean independentMobility; // 스스로거동가능

        private boolean mobilityAssist; // 이동시 부축도움

        private boolean wheelchairAssist; // 휠체어 이동 보조

        private boolean immobile; // 거동 불가

        private boolean cleaningLaundryAssist; // 청소 빨래 보조

        private boolean bathingAssist; // 목욕 보조

        private boolean hospitalAccompaniment; // 병원 보조

        private boolean exerciseSupport; // 산책, 간단한 운동

        private boolean emotionalSupport; // 정서적 지원

        private boolean cognitiveStimulation; // 인지 자극 활동

        private List<RequestTimeDto> recruitTimes;
    }

    @Getter
    public static class UpdateRequestDto {

        private Long elderId;

        private CareType careType; // 근무 종류

        private boolean flexibleSchedule; // 시간 협의 여부

        private Integer desiredHourlyWage; // 희망 급여

        private boolean selfFeeding; // 스스로 식사 가능

        private boolean mealPreparation; // 식사 차려드리기

        private boolean cookingAssistance; // 요리 필요

        private boolean enteralNutritionSupport; // 경관식 보조

        private boolean selfToileting; // 스스로 배변 가능

        private boolean occasionalToiletingAssist; // 가끔 대소변 실수

        private boolean diaperCare; // 기저귀 케어 필요

        private boolean catheterOrStomaCare; // 유치도뇨/방광루/장루 관리

        private boolean independentMobility; // 스스로거동가능

        private boolean mobilityAssist; // 이동시 부축도움

        private boolean wheelchairAssist; // 휠체어 이동 보조

        private boolean immobile; // 거동 불가

        private boolean cleaningLaundryAssist; // 청소 빨래 보조

        private boolean bathingAssist; // 목욕 보조

        private boolean hospitalAccompaniment; // 병원 보조

        private boolean exerciseSupport; // 산책, 간단한 운동

        private boolean emotionalSupport; // 정서적 지원

        private boolean cognitiveStimulation; // 인지 자극 활동

        private List<RequestTimeDto> recruitTimes;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestTimeDto {

        private String dayOfWeek;

        @JsonFormat(pattern = "HH:mm")
        private LocalTime startTime;

        @JsonFormat(pattern = "HH:mm")
        private LocalTime endTime;
    }
}
