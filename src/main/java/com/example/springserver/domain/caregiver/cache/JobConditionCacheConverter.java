package com.example.springserver.domain.caregiver.cache;

import com.example.springserver.domain.caregiver.dto.response.JobConditionResponseDto;
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
                        .map(JobConditionCacheConverter::toWorkLocationCache)
                        .collect(Collectors.toList()))
                .caregiverId(jobCondition.getCaregiver().getId())
                .build();
    }

    // Redis 저장 객체 -> jobConditionDto 컨버팅
    public static JobConditionResponseDto.JobConditionResponseDTO fromRedisDto(JobConditionCache cache) {
        return JobConditionResponseDto.JobConditionResponseDTO.builder()
                .jobConditionId(cache.getId())
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
                .endTime(cache.getEndTime())
                .locationResponseDtoList(toLocationResponseDtoList(cache.getWorkLocations()))
                .caregiverId(cache.getCaregiverId())
                .build();
    }

    private static List<JobConditionResponseDto.LocationResponseDTO> toLocationResponseDtoList(List<WorkLocationCache> workLocationIds) {
        return workLocationIds.stream()
                .map(w -> JobConditionResponseDto.LocationResponseDTO.builder()
                        .workLocationId(w.getWorkLocationId())
                        .locationName(w.getAddress())
                        .build()
                )
                .collect(Collectors.toList());
    }

    private static WorkLocationCache toWorkLocationCache(WorkLocation workLocation) {
        return WorkLocationCache.builder()
                .workLocationId(workLocation.getId())
                .locationId(workLocation.getLocationId().getLocationId())
                .address(workLocation.getLocationId().getAddress())
                .build();
    }
}
