package com.example.springserver.domain.caregiver.dto.response;

import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDTO;
import com.example.springserver.domain.caregiver.entity.enums.CertType;
import com.example.springserver.domain.caregiver.entity.enums.Level;
import com.example.springserver.domain.caregiver.entity.enums.Week;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class CaregiverResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUpCaregiverResult {
        private Long caregiverId;
        private String createAt;
    }

    @Builder
    @Getter
    public static class CareGiverInfoResponseDTO{

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
    }

    @Getter
    @Builder
    public static class JobConditionResponseDTO {

        private Long jobConditionId;

        private Boolean flexibleSchedule;

        private Boolean desiredHourlyWage;

        private Boolean selfFeeding;

        private Boolean mealPreparation;

        private Boolean cookingAssistance;

        private Boolean enteralNutritionSupport;

        private Boolean selfToileting;

        private Boolean occasionalToiletingAssist;

        private Boolean diaperCare;

        private Boolean catheterOrStomaCare;

        private Boolean independentMobility;

        private Boolean mobilityAssist;

        private Boolean wheelchairAssist;

        private Boolean immobile;

        private Boolean cleaningLaundryAssist;

        private Boolean bathingAssist;

        private Boolean hospitalAccompaniment;

        private Boolean exerciseSupport;

        private Boolean emotionalSupport;

        private Boolean cognitiveStimulation;

        private List<LocationResponseDTO> locationRequestDTOList;

        private List<WorkTimeResponseDTO> workTimeRequestDTOList;
    }

    @Getter
    @Builder
    public static class LocationResponseDTO{

        private Long workLocationId;

        private String locationName;

    }

    @Getter
    @Builder
    public static class WorkTimeResponseDTO{

        private Long workTimeId;

        private Week dayOfWeek;

        private LocalDateTime start_time;

        private LocalDateTime end_time;

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
