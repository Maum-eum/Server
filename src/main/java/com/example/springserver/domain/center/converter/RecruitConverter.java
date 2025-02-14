package com.example.springserver.domain.center.converter;

import com.example.springserver.domain.center.dto.request.RecruitRequestDto.RequestTimeDto;
import com.example.springserver.domain.center.dto.response.RecruitResponseDto.*;
import com.example.springserver.domain.center.entity.Elder;
import com.example.springserver.domain.center.entity.RecruitCondition;
import com.example.springserver.domain.center.dto.request.RecruitRequestDto.CreateReqDto;
import com.example.springserver.domain.center.entity.RecruitTime;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecruitConverter {

    public static RecruitCondition toRecruitCondition(CreateReqDto createReqDto, Elder elder) {
         return RecruitCondition.builder()
                .elder(elder)
                .careType(createReqDto.getCareType())
                .flexibleSchedule(createReqDto.isFlexibleSchedule())
                .recruitTimes(new ArrayList<>())
                .desiredHourlyWage(createReqDto.getDesiredHourlyWage())
                .selfFeeding(createReqDto.isSelfFeeding())
                .mealPreparation(createReqDto.isMealPreparation())
                .cookingAssistance(createReqDto.isCookingAssistance())
                .enteralNutritionSupport(createReqDto.isEnteralNutritionSupport())
                .selfToileting(createReqDto.isSelfToileting())
                .occasionalToiletingAssist(createReqDto.isOccasionalToiletingAssist())
                .diaperCare(createReqDto.isDiaperCare())
                .catheterOrStomaCare(createReqDto.isCatheterOrStomaCare())
                .independentMobility(createReqDto.isIndependentMobility())
                .mobilityAssist(createReqDto.isMobilityAssist())
                .wheelchairAssist(createReqDto.isWheelchairAssist())
                .immobile(createReqDto.isImmobile())
                .cleaningLaundryAssist(createReqDto.isCleaningLaundryAssist())
                .bathingAssist(createReqDto.isBathingAssist())
                .hospitalAccompaniment(createReqDto.isHospitalAccompaniment())
                .exerciseSupport(createReqDto.isExerciseSupport())
                .emotionalSupport(createReqDto.isEmotionalSupport())
                .cognitiveStimulation(createReqDto.isCognitiveStimulation())
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
                .careType(recruitCondition.getCareType())
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
