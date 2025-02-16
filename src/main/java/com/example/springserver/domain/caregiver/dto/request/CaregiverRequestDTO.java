package com.example.springserver.domain.caregiver.dto.request;

import com.example.springserver.domain.caregiver.entity.enums.CertType;
import com.example.springserver.domain.caregiver.entity.enums.Level;
import com.example.springserver.domain.caregiver.entity.enums.ScheduleAvailability;
import com.example.springserver.domain.center.entity.enums.RecruitStatus;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

public class CaregiverRequestDTO {

    @Getter
    public static class SignUpCaregiverReq{

        @NotEmpty
        private String username;

        @NotEmpty
        private String password;

        @NotEmpty
        private String name;

        @NotEmpty
        private String contact;

        @NotNull
        private Boolean car;

        @NotNull
        private Boolean education;

        private String img;

        private String intro;

        private String address;

        private Boolean employmentStatus;

        private List<CertificateRequestDTO> certificateRequestDTOList;

        private List<ExperienceRequestDTO> experienceRequestDTOList;


        public SignUpCaregiverReq() {
            this.car = false; // 기본값 false
            this.education = false;   // 기본값 false
        }

        public void setCommonImg(){
            this.img = "http://localhost:8080/basicImg.jpeg";
        }
    }

    @Getter
    public static class UpdateCaregiverReq{

        @NotEmpty
        private String username;

        @NotEmpty
        private String contact;

        @NotNull
        private Boolean car;

        @NotNull
        private Boolean education;

        private String img;

        private String intro;

        private String address;

        private List<CertificateRequestDTO> certificateRequestDTOList;

        private List<ExperienceRequestDTO> experienceRequestDTOList;

    }

    @Getter
    public static class JobConditionRequestDTO {

        private ScheduleAvailability flexibleSchedule;

        private ScheduleAvailability desiredHourlyWage;

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
    public static class RecruitReq {

        private Long matchId;

        private RecruitStatus status;

    }

    @Getter
    public static class LocationRequestDTO{

        private Long workLocationId;

        @NotNull
        private Long locationId;

    }


    @Getter
    public static class CertificateRequestDTO{

        private String certNum;

        @NotNull
        private CertType certType;

        @NotNull
        private Level certRate;

    }

    @Getter
    public static class ExperienceRequestDTO{

        @NotNull
        private int duration;

        @NotEmpty
        private String title;

        @NotEmpty
        private String description;

    }
}
