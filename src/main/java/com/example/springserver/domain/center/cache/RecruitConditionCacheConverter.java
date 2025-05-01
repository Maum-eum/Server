package com.example.springserver.domain.center.cache;

import com.example.springserver.domain.center.converter.RecruitConverter;
import com.example.springserver.domain.center.dto.RecruitConditionOptionInfo;
import com.example.springserver.domain.center.dto.response.RecruitResponseDto;
import com.example.springserver.domain.center.entity.RecruitCondition;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RecruitConditionCacheConverter {

    public static RecruitConditionCache toCache(RecruitCondition condition) {
        return RecruitConditionCache.builder()
                .id(condition.getRecruitConditionId())
                .elderId(condition.getElder().getElderId())
                .locationId(condition.getRecruitConditionId())
                .careTypes(condition.getCareTypes())
                .flexibleSchedule(condition.isFlexibleSchedule())
                .address(condition.getAddress())
                .recruitTimes(RecruitConverter.toRecruitResponseTimeListDto(condition.getRecruitTimes()))
                .desiredHourlyWage(condition.getDesiredHourlyWage())
                .selfFeeding(condition.isSelfFeeding())
                .mealPreparation(condition.isMealPreparation())
                .cookingAssistance(condition.isCookingAssistance())
                .enteralNutritionSupport(condition.isEnteralNutritionSupport())
                .selfToileting(condition.isSelfToileting())
                .occasionalToiletingAssist(condition.isOccasionalToiletingAssist())
                .diaperCare(condition.isDiaperCare())
                .catheterOrStomaCare(condition.isCatheterOrStomaCare())
                .independentMobility(condition.isIndependentMobility())
                .mobilityAssist(condition.isMobilityAssist())
                .wheelchairAssist(condition.isWheelchairAssist())
                .immobile(condition.isImmobile())
                .cleaningLaundryAssist(condition.isCleaningLaundryAssist())
                .bathingAssist(condition.isBathingAssist())
                .hospitalAccompaniment(condition.isHospitalAccompaniment())
                .exerciseSupport(condition.isExerciseSupport())
                .emotionalSupport(condition.isEmotionalSupport())
                .cognitiveStimulation(condition.isCognitiveStimulation())
                .detailRequiredService(condition.getDetailRequiredService())
                .build();
    }

    public static List<RecruitConditionCache> toCacheList(List<RecruitCondition> conditionList) {
        return conditionList.stream()
                .map(RecruitConditionCacheConverter::toCache)
                .collect(Collectors.toList());
    }

    public static RecruitResponseDto.Response fromCache(RecruitConditionCache cache) {
        return RecruitResponseDto.Response.builder()
                .recruitConditionId(cache.getId())
                .elderId(cache.getElderId())
                .recruitLocation(cache.getLocationId())
                .careTypes(cache.getCareTypes())
                .address(cache.getAddress())
                .recruitTimes(cache.getRecruitTimes())
                .recruitConditionOptionInfo(
                        RecruitConditionOptionInfo.builder()
                                .flexibleSchedule(cache.isFlexibleSchedule())
                                .mealAssistance(cache.isMealAssistance())
                                .toiletAssistance(cache.isToiletAssistance())
                                .moveAssistance(cache.isMoveAssistance())
                                .dailyLivingAssistance(cache.isDailyLivingAssistance())
                                .desiredHourlyWage(cache.getDesiredHourlyWage())
                                .selfFeeding(cache.isSelfFeeding())
                                .mealPreparation(cache.isMealPreparation())
                                .cookingAssistance(cache.isCookingAssistance())
                                .enteralNutritionSupport(cache.isEnteralNutritionSupport())
                                .selfToileting(cache.isSelfToileting())
                                .occasionalToiletingAssist(cache.isOccasionalToiletingAssist())
                                .diaperCare(cache.isDiaperCare())
                                .catheterOrStomaCare(cache.isCatheterOrStomaCare())
                                .independentMobility(cache.isIndependentMobility())
                                .mobilityAssist(cache.isMobilityAssist())
                                .wheelchairAssist(cache.isWheelchairAssist())
                                .immobile(cache.isImmobile())
                                .cleaningLaundryAssist(cache.isCleaningLaundryAssist())
                                .bathingAssist(cache.isBathingAssist())
                                .hospitalAccompaniment(cache.isHospitalAccompaniment())
                                .exerciseSupport(cache.isExerciseSupport())
                                .emotionalSupport(cache.isEmotionalSupport())
                                .cognitiveStimulation(cache.isCognitiveStimulation())
                                .build()
                )
                .detailRequiredService(cache.getDetailRequiredService())
                .build();
    }

    public static List<RecruitResponseDto.Response> fromCacheList(List<RecruitConditionCache> cacheList) {
        return cacheList.stream()
                .map(RecruitConditionCacheConverter::fromCache)
                .collect(Collectors.toList());
    }
}