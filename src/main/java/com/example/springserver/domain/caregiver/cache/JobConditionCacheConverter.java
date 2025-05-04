package com.example.springserver.domain.caregiver.cache;

import com.example.springserver.domain.caregiver.dto.JobConditionOptionInfo;
import com.example.springserver.domain.caregiver.dto.response.JobConditionResponseDto;
import com.example.springserver.domain.caregiver.entity.Caregiver;
import com.example.springserver.domain.caregiver.entity.JobCondition;
import com.example.springserver.domain.caregiver.entity.WorkLocation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JobConditionCacheConverter {

    // JobCondition -> Redis 저장 객체로 컨버팅
    public static JobConditionCache toRedisDto(JobCondition jobCondition) {
        return JobConditionCache.builder()
                .id(jobCondition.getId())
                .flexibleSchedule(jobCondition.getFlexibleSchedule())
                .desiredHourlyWage(jobCondition.getDesiredHourlyWage())
                .selfFeeding(jobCondition.getSelfFeeding())
                .mealPreparation(jobCondition.getMealPreparation())
                .cookingAssistance(jobCondition.getCookingAssistance())
                .enteralNutritionSupport(jobCondition.getEnteralNutritionSupport())
                .selfToileting(jobCondition.getSelfToileting())
                .occasionalToiletingAssist(jobCondition.getOccasionalToiletingAssist())
                .diaperCare(jobCondition.getDiaperCare())
                .catheterOrStomaCare(jobCondition.getCatheterOrStomaCare())
                .independentMobility(jobCondition.getIndependentMobility())
                .mobilityAssist(jobCondition.getMobilityAssist())
                .wheelchairAssist(jobCondition.getWheelchairAssist())
                .immobile(jobCondition.getImmobile())
                .cleaningLaundryAssist(jobCondition.getCleaningLaundryAssist())
                .bathingAssist(jobCondition.getBathingAssist())
                .hospitalAccompaniment(jobCondition.getHospitalAccompaniment())
                .exerciseSupport(jobCondition.getExerciseSupport())
                .emotionalSupport(jobCondition.getEmotionalSupport())
                .cognitiveStimulation(jobCondition.getCognitiveStimulation())
                .dayOfWeek(jobCondition.getDayOfWeek())
                .startTime(jobCondition.getStartTime())
                .endTime(jobCondition.getEndTime())
                .workLocations(jobCondition.getWorkLocations().stream()
                        .map(JobConditionCacheConverter::toWorkLocationRedisDto)
                        .collect(Collectors.toList()))
                .caregiverId(jobCondition.getCaregiver().getId())
                .build();
    }

    public static JobCondition toEntity(JobConditionCache cache, Caregiver caregiver, List<WorkLocation> workLocations) {
        return JobCondition.builder()
                .flexibleSchedule(cache.getFlexibleSchedule())
                .desiredHourlyWage(cache.getDesiredHourlyWage())
                .selfFeeding(cache.getSelfFeeding())
                .mealPreparation(cache.getMealPreparation())
                .cookingAssistance(cache.getCookingAssistance())
                .enteralNutritionSupport(cache.getEnteralNutritionSupport())
                .selfToileting(cache.getSelfToileting())
                .occasionalToiletingAssist(cache.getOccasionalToiletingAssist())
                .diaperCare(cache.getDiaperCare())
                .catheterOrStomaCare(cache.getCatheterOrStomaCare())
                .independentMobility(cache.getIndependentMobility())
                .mobilityAssist(cache.getMobilityAssist())
                .wheelchairAssist(cache.getWheelchairAssist())
                .immobile(cache.getImmobile())
                .cleaningLaundryAssist(cache.getCleaningLaundryAssist())
                .bathingAssist(cache.getBathingAssist())
                .hospitalAccompaniment(cache.getHospitalAccompaniment())
                .exerciseSupport(cache.getExerciseSupport())
                .emotionalSupport(cache.getEmotionalSupport())
                .cognitiveStimulation(cache.getCognitiveStimulation())
                .dayOfWeek(cache.getDayOfWeek())
                .startTime(cache.getStartTime())
                .endTime(cache.getEndTime())
                .caregiver(caregiver)
                .workLocations(workLocations)
                .build();
    }

    // Redis 저장 객체 -> jobConditionDto 컨버팅
    public static JobConditionResponseDto.Response fromRedisDto(JobConditionCache cache) {
        return JobConditionResponseDto.Response.builder()
                .jobConditionId(cache.getId())
                .jobConditionOptionInfo(
                        JobConditionOptionInfo.builder()
                                .flexibleSchedule(cache.getFlexibleSchedule())
                                .desiredHourlyWage(cache.getDesiredHourlyWage())
                                .selfFeeding(cache.getSelfFeeding())
                                .mealPreparation(cache.getMealPreparation())
                                .cookingAssistance(cache.getCookingAssistance())
                                .enteralNutritionSupport(cache.getEnteralNutritionSupport())
                                .selfToileting(cache.getSelfToileting())
                                .occasionalToiletingAssist(cache.getOccasionalToiletingAssist())
                                .diaperCare(cache.getDiaperCare())
                                .catheterOrStomaCare(cache.getCatheterOrStomaCare())
                                .independentMobility(cache.getIndependentMobility())
                                .mobilityAssist(cache.getMobilityAssist())
                                .wheelchairAssist(cache.getWheelchairAssist())
                                .immobile(cache.getImmobile())
                                .cleaningLaundryAssist(cache.getCleaningLaundryAssist())
                                .bathingAssist(cache.getBathingAssist())
                                .hospitalAccompaniment(cache.getHospitalAccompaniment())
                                .exerciseSupport(cache.getExerciseSupport())
                                .emotionalSupport(cache.getEmotionalSupport())
                                .cognitiveStimulation(cache.getCognitiveStimulation())
                                .dayOfWeek(String.valueOf(cache.getDayOfWeek()))
                                .startTime(cache.getStartTime())
                                .endTime(cache.getEndTime()).build()
                )
                .locationResponseList(toWorkLocationResponseList(cache.getWorkLocations()))
                .caregiverId(cache.getCaregiverId())
                .build();
    }

    private static List<JobConditionResponseDto.LocationResponse> toWorkLocationResponseList(List<WorkLocationCache> workLocationIds) {
        return workLocationIds.stream()
                .map(w -> JobConditionResponseDto.LocationResponse.builder()
                        .workLocationId(w.getWorkLocationId())
                        .locationName(w.getAddress())
                        .build()
                )
                .collect(Collectors.toList());
    }

    private static WorkLocationCache toWorkLocationRedisDto(WorkLocation workLocation) {
        return WorkLocationCache.builder()
                .workLocationId(workLocation.getId())
                .locationId(workLocation.getLocation().getLocationId())
                .address(workLocation.getLocation().getAddress())
                .build();
    }
}
