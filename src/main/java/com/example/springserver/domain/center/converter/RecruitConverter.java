package com.example.springserver.domain.center.converter;

import com.example.springserver.domain.center.dto.request.RecruitRequestDto.RequestTimeDto;
import com.example.springserver.domain.center.dto.response.RecruitResponseDto.*;
import com.example.springserver.domain.center.entity.Elder;
import com.example.springserver.domain.center.entity.RecruitCondition;
import com.example.springserver.domain.center.dto.request.RecruitRequestDto.RequestDto;
import com.example.springserver.domain.center.entity.RecruitTime;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecruitConverter {

    public static RecruitCondition toRecruitCondition(RequestDto requestDto, Elder elder) {
         return RecruitCondition.builder()
                .elder(elder)
                .careTypes(requestDto.getCareTypes())
                .flexibleSchedule(requestDto.isFlexibleSchedule())
                .recruitTimes(new ArrayList<>())
                 .mealAssistance(requestDto.isMealAssistance())
                 .toiletAssistance(requestDto.isToiletAssistance())
                 .moveAssistance(requestDto.isMoveAssistance())
                 .dailyLivingAssistance(requestDto.isDailyLivingAssistance())
                .desiredHourlyWage(requestDto.getDesiredHourlyWage())
                .selfFeeding(requestDto.isSelfFeeding())
                .mealPreparation(requestDto.isMealPreparation())
                .cookingAssistance(requestDto.isCookingAssistance())
                .enteralNutritionSupport(requestDto.isEnteralNutritionSupport())
                .selfToileting(requestDto.isSelfToileting())
                .occasionalToiletingAssist(requestDto.isOccasionalToiletingAssist())
                .diaperCare(requestDto.isDiaperCare())
                .catheterOrStomaCare(requestDto.isCatheterOrStomaCare())
                .independentMobility(requestDto.isIndependentMobility())
                .mobilityAssist(requestDto.isMobilityAssist())
                .wheelchairAssist(requestDto.isWheelchairAssist())
                .immobile(requestDto.isImmobile())
                .cleaningLaundryAssist(requestDto.isCleaningLaundryAssist())
                .bathingAssist(requestDto.isBathingAssist())
                .hospitalAccompaniment(requestDto.isHospitalAccompaniment())
                .exerciseSupport(requestDto.isExerciseSupport())
                .emotionalSupport(requestDto.isEmotionalSupport())
                .cognitiveStimulation(requestDto.isCognitiveStimulation())
                 .detailRequiredService(requestDto.getDetailRequiredService())
                .build();
    }

    public static RecruitTime toRecruitTime(RequestTimeDto createReqTimeDto, RecruitCondition recruitcondition) {
        return RecruitTime.builder()
                .recruitCondition(recruitcondition)
                .startTime(createReqTimeDto.getStartTime())
                .endTime(createReqTimeDto.getEndTime())
                .dayOfWeek(createReqTimeDto.getDayOfWeek())
                .build();
    }

    public static ResponseDto toConditionResponseDto(RecruitCondition recruitCondition) {
        return ResponseDto.builder()
                .recruitConditionId(recruitCondition.getRecruitConditionId())
                .elderId(recruitCondition.getElder().getElderId())
                .careTypes(recruitCondition.getCareTypes())
                .mealAssistance(recruitCondition.isMealAssistance())
                .toiletAssistance(recruitCondition.isToiletAssistance())
                .moveAssistance(recruitCondition.isMoveAssistance())
                .dailyLivingAssistance(recruitCondition.isDailyLivingAssistance())
                .flexibleSchedule(recruitCondition.isFlexibleSchedule())
                .recruitTimes(toRecruitTimeListDto(recruitCondition.getRecruitTimes()))
                .desiredHourlyWage(recruitCondition.getDesiredHourlyWage())
                .selfFeeding(recruitCondition.isSelfFeeding())
                .mealPreparation(recruitCondition.isMealPreparation())
                .cookingAssistance(recruitCondition.isCookingAssistance())
                .enteralNutritionSupport(recruitCondition.isEnteralNutritionSupport())
                .selfToileting(recruitCondition.isSelfToileting())
                .occasionalToiletingAssist(recruitCondition.isOccasionalToiletingAssist())
                .diaperCare(recruitCondition.isDiaperCare())
                .catheterOrStomaCare(recruitCondition.isCatheterOrStomaCare())
                .independentMobility(recruitCondition.isIndependentMobility())
                .mobilityAssist(recruitCondition.isMobilityAssist())
                .wheelchairAssist(recruitCondition.isWheelchairAssist())
                .immobile(recruitCondition.isImmobile())
                .cleaningLaundryAssist(recruitCondition.isCleaningLaundryAssist())
                .bathingAssist(recruitCondition.isBathingAssist())
                .hospitalAccompaniment(recruitCondition.isHospitalAccompaniment())
                .exerciseSupport(recruitCondition.isExerciseSupport())
                .emotionalSupport(recruitCondition.isEmotionalSupport())
                .cognitiveStimulation(recruitCondition.isCognitiveStimulation())
                .detailRequiredService(recruitCondition.getDetailRequiredService())
                .build();
    }

    public static RequestTimeDto toTimeResponseDto(RecruitTime recruitTime) {
        return RequestTimeDto.builder()
                .startTime(recruitTime.getStartTime())
                .endTime(recruitTime.getEndTime())
                .dayOfWeek(recruitTime.getDayOfWeek())
                .build();
    }

    public static List<ResponseDto> toListResponseDto(List<RecruitCondition> recruitConditionList) {
        return recruitConditionList.stream()
                .map(RecruitConverter::toConditionResponseDto)
                .collect(Collectors.toList());
    }

    public static List<RequestTimeDto> toRecruitTimeListDto(List<RecruitTime> recruitTimeList) {
        return recruitTimeList.stream()
                .map(RecruitConverter::toTimeResponseDto)
                .collect(Collectors.toList());
    }
}
