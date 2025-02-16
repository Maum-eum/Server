package com.example.springserver.domain.caregiver.dto.request;

import com.example.springserver.domain.caregiver.entity.enums.CertType;
import com.example.springserver.domain.caregiver.entity.enums.Level;
import com.example.springserver.domain.caregiver.entity.enums.ScheduleAvailability;
import com.example.springserver.domain.center.entity.enums.RecruitStatus;
import com.example.springserver.domain.center.entity.enums.Week;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class JobConditionRequestDto {

    @Getter
    public static class JobConditionReqDto {

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

        private List<LocationRequestDTO> locationRequestDTOList;


    }
    @Getter
    public static class LocationRequestDTO{

        private Long workLocationId;

        private Long locationId;

    }
}
