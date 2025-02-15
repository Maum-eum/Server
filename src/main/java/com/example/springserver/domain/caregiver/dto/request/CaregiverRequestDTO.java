package com.example.springserver.domain.caregiver.dto.request;

import com.example.springserver.domain.caregiver.entity.enums.CertType;
import com.example.springserver.domain.caregiver.entity.enums.Level;
import com.example.springserver.domain.caregiver.entity.enums.Week;
import com.example.springserver.domain.center.entity.enums.RecruitStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.Instant;
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

        private Boolean flexibleSchedule = false;

        private Boolean desiredHourlyWage = false;

        private Boolean selfFeeding = false;

        private Boolean mealPreparation = false;

        private Boolean cookingAssistance = false;

        private Boolean enteralNutritionSupport = false;

        private Boolean selfToileting = false;

        private Boolean occasionalToiletingAssist = false;

        private Boolean diaperCare = false;

        private Boolean catheterOrStomaCare = false;

        private Boolean independentMobility = false;

        private Boolean mobilityAssist = false;

        private Boolean wheelchairAssist = false;

        private Boolean immobile = false;

        private Boolean cleaningLaundryAssist = false;

        private Boolean bathingAssist = false;

        private Boolean hospitalAccompaniment = false;

        private Boolean exerciseSupport = false;

        private Boolean emotionalSupport = false;

        private Boolean cognitiveStimulation = false;

        private List<LocationRequestDTO> locationRequestDTOList;

        private List<WorkTimeRequestDTO> workTimeRequestDTOList;
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
    public static class WorkTimeRequestDTO{

        private Long workTimeId;

        @NotNull
        private Week dayOfWeek;

        @NotNull
        private int startTime;

        @NotNull
        private int endTime;

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
