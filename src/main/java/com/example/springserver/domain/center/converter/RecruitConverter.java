package com.example.springserver.domain.center.converter;

import com.example.springserver.domain.center.dto.request.RecruitRequestDto.RequestDto;
import com.example.springserver.domain.center.dto.request.RecruitRequestDto.RequestTimeDto;
import com.example.springserver.domain.center.dto.response.RecruitResponseDto.ResponseDto;
import com.example.springserver.domain.center.dto.response.RecruitResponseDto.ResponseTimeDto;
import com.example.springserver.domain.center.entity.Elder;
import com.example.springserver.domain.center.entity.RecruitCondition;
import com.example.springserver.domain.center.entity.RecruitTime;
import com.example.springserver.domain.location.entity.Location;

import java.util.List;
import java.util.stream.Collectors;

public class RecruitConverter {

    public static RecruitCondition toRecruitCondition(RequestDto requestDto, Elder elder, Location location) {

        RecruitCondition recruitCondition = RecruitCondition.builder()
                .elder(elder)
                .careTypes(requestDto.getCareTypes())
                .flexibleSchedule(requestDto.isFlexibleSchedule())
                .address(location.getAddress())
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

        List<RecruitTime> recruitTimes = toRecruitTimeList(requestDto.getRecruitTimes(), recruitCondition);
        recruitCondition.updateRecruitTimes(recruitTimes);
        recruitCondition.setRecruitLocation(location);

        return recruitCondition;
    }

    public static RecruitTime toRecruitTime(RequestTimeDto createReqTimeDto, RecruitCondition recruitcondition) {
        return RecruitTime.builder()
                .recruitCondition(recruitcondition)
                .startTime(createReqTimeDto.getStartTime())
                .endTime(createReqTimeDto.getEndTime())
                .dayOfWeek(createReqTimeDto.getDayOfWeek())
                .build();
    }

    public static List<RecruitTime> toRecruitTimeList(List<RequestTimeDto> recruitTimeDtoList, RecruitCondition recruitcondition) {
        return recruitTimeDtoList.stream()
                .map(dto -> toRecruitTime(dto, recruitcondition))
                .collect(Collectors.toList());
    }

    public static ResponseDto toConditionResponseDto(RecruitCondition recruitCondition) {
        return ResponseDto.builder()
                .recruitConditionId(recruitCondition.getRecruitConditionId())
                .elderId(recruitCondition.getElder().getElderId())
                .careTypes(recruitCondition.getCareTypes())
                .recruitLocation(recruitCondition.getRecruitLocation().getLocationId())
                .mealAssistance(recruitCondition.isMealAssistance())
                .toiletAssistance(recruitCondition.isToiletAssistance())
                .moveAssistance(recruitCondition.isMoveAssistance())
                .dailyLivingAssistance(recruitCondition.isDailyLivingAssistance())
                .flexibleSchedule(recruitCondition.isFlexibleSchedule())
                .recruitTimes(toRecruitResponseTimeListDto(recruitCondition.getRecruitTimes()))
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

    // 모집 시간 convert
    public static ResponseTimeDto toTimeResponseDto(RecruitTime recruitTime) {
        return ResponseTimeDto.builder()
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

    public static List<ResponseTimeDto> toRecruitResponseTimeListDto(List<RecruitTime> recruitTimeList) {
        return recruitTimeList.stream()
                .map(RecruitConverter::toTimeResponseDto)
                .collect(Collectors.toList());
    }
}
