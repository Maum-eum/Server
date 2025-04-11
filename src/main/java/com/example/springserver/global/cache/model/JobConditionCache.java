package com.example.springserver.global.cache.model;

import com.example.springserver.domain.caregiver.entity.enums.ScheduleAvailability;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("job_condition")
public class JobConditionCache implements Serializable {
    @Id
    private Long id;

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

    private Integer dayOfWeek;
    private Long startTime;
    private Long endTime;

    private List<Long> workLocationIds;
    private Long caregiverId;
}