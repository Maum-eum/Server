package com.example.springserver.domain.caregiver.dto.response;

import com.example.springserver.domain.caregiver.entity.enums.CertType;
import com.example.springserver.domain.caregiver.entity.enums.Level;
import com.example.springserver.domain.caregiver.entity.enums.ScheduleAvailability;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class JobConditionResponseDto {

    @Getter
    @Builder
    public static class JobConditionResponseDTO {

        private Long jobConditionId;

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

        private List<LocationResponseDTO> locationRequestDtoList;
    }

    @Getter
    @Builder
    public static class DetailJobConditionResponseDTO {

        //CareGiver info

        private String name;

        private String contact;

        private Boolean car;

        private Boolean education;

        private String img;

        private String intro;

        private String address;

        private Boolean employmentStatus;

        private List<CertificateResponseDTO> certificateResponseDTOList;

        private List<ExperienceResponseDTO> experienceResponseDTOList ;

        //jobCondition

        private Long jobConditionId;

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

        private List<LocationResponseDTO> locationRequestDTOList;

    }

    @Getter
    @Builder
    public static class LocationResponseDTO{

        private Long workLocationId;

        private String locationName;

    }

    @Getter
    @Builder
    public static class CertificateResponseDTO{

        private String certNum;

        private CertType certType;

        private Level certRate;

    }

    @Getter
    @Builder
    public static class ExperienceResponseDTO{

        private int duration;

        private String title;

        private String description;

    }
}
