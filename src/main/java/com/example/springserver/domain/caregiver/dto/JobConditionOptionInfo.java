package com.example.springserver.domain.caregiver.dto;

import com.example.springserver.domain.caregiver.entity.enums.ScheduleAvailability;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class JobConditionOptionInfo {
    private ScheduleAvailability flexibleSchedule;
    private Integer desiredHourlyWage;
    private ScheduleAvailability selfFeeding;
    private ScheduleAvailability mealPreparation;
    private ScheduleAvailability cookingAssistance;
    private ScheduleAvailability enteralNutritionSupport;
    private ScheduleAvailability selfToileting;
    private ScheduleAvailability occasionalToiletingAssist;
    private ScheduleAvailability diaperCare;
    private ScheduleAvailability catheterOrStomaCare;
    private ScheduleAvailability independentMobility;
    private ScheduleAvailability mobilityAssist;
    private ScheduleAvailability wheelchairAssist;
    private ScheduleAvailability immobile;
    private ScheduleAvailability cleaningLaundryAssist;
    private ScheduleAvailability bathingAssist;
    private ScheduleAvailability hospitalAccompaniment;
    private ScheduleAvailability exerciseSupport;
    private ScheduleAvailability emotionalSupport;
    private ScheduleAvailability cognitiveStimulation;
    private String dayOfWeek;
    private Long startTime;
    private Long endTime;
}
