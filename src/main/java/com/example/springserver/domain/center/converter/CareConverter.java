package com.example.springserver.domain.center.converter;

import com.example.springserver.domain.center.dto.response.CareResponseDto.*;
import com.example.springserver.domain.center.entity.Care;
import com.example.springserver.domain.center.entity.Elder;
import com.example.springserver.domain.center.dto.request.CareRequestDto.RequestDto;
import com.example.springserver.domain.location.entity.Location;

import java.util.List;
import java.util.stream.Collectors;

public class CareConverter {

    public static Care toCare(RequestDto requestDto, Elder elder, Location location) {
        return Care.builder()
                .elder(elder)
                .careTypes(requestDto.getCareTypes())
                .careLocation(location)
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

    public static ResponseDto toConditionResponseDto(Care care) {
        return ResponseDto.builder()
                .recruitConditionId(care.getCareId())
                .elderId(care.getElder().getElderId())
                .careTypes(care.getCareTypes())
                .careLocation(care.getCareLocation().getLocationId())
                .mealAssistance(care.isMealAssistance())
                .toiletAssistance(care.isToiletAssistance())
                .moveAssistance(care.isMoveAssistance())
                .dailyLivingAssistance(care.isDailyLivingAssistance())
                .desiredHourlyWage(care.getDesiredHourlyWage())
                .selfFeeding(care.isSelfFeeding())
                .mealPreparation(care.isMealPreparation())
                .cookingAssistance(care.isCookingAssistance())
                .enteralNutritionSupport(care.isEnteralNutritionSupport())
                .selfToileting(care.isSelfToileting())
                .occasionalToiletingAssist(care.isOccasionalToiletingAssist())
                .diaperCare(care.isDiaperCare())
                .catheterOrStomaCare(care.isCatheterOrStomaCare())
                .independentMobility(care.isIndependentMobility())
                .mobilityAssist(care.isMobilityAssist())
                .wheelchairAssist(care.isWheelchairAssist())
                .immobile(care.isImmobile())
                .cleaningLaundryAssist(care.isCleaningLaundryAssist())
                .bathingAssist(care.isBathingAssist())
                .hospitalAccompaniment(care.isHospitalAccompaniment())
                .exerciseSupport(care.isExerciseSupport())
                .emotionalSupport(care.isEmotionalSupport())
                .cognitiveStimulation(care.isCognitiveStimulation())
                .detailRequiredService(care.getDetailRequiredService())
                .build();
    }

    public static List<ResponseDto> toListResponseDto(List<Care> recruitConditionList) {
        return recruitConditionList.stream()
                .map(CareConverter::toConditionResponseDto)
                .collect(Collectors.toList());
    }
}
