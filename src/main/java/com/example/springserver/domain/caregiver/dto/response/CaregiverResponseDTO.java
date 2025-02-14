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

        private List<CertificateResponseDTO> certificateResponseDTOList;

        private List<ExperienceResponseDTO> experienceResponseDTOList ;
    }

    @Getter
    @Builder
    public static class JobConditionResponseDTO {

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

        @NotNull
        private CertType certType;

        @NotNull
        private Level certRate;

    }

    @Getter
    @Builder
    public static class ExperienceResponseDTO{

        @NotNull
        private int duration;

        @NotEmpty
        private String title;

        @NotEmpty
        private String description;

    }

}
