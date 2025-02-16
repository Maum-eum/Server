package com.example.springserver.domain.center.dto.request;

import com.example.springserver.domain.center.entity.enums.CareType;
import com.example.springserver.domain.center.entity.enums.Week;
import com.example.springserver.domain.location.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class RecruitRequestDto {

    @Getter
    public static class RequestDto {

        private List<CareType> careTypes; // 근무 종류

        private Long recruitLocation;

        private boolean mealAssistance;

        private boolean toiletAssistance;

        private boolean moveAssistance;

        private boolean dailyLivingAssistance;

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

        private String detailRequiredService; // 추가 요청 사항
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestTimeDto {

        private Week dayOfWeek;

        private Long startTime;

        private Long endTime;
    }
}
