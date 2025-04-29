package com.example.springserver.domain.caregiver.dto.request;

import com.example.springserver.domain.caregiver.dto.CaregiverBasicInfo;
import com.example.springserver.domain.caregiver.entity.enums.CertType;
import com.example.springserver.domain.caregiver.entity.enums.Level;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

public class CaregiverRequestDto {

    @Getter
    public static class CaregiverSignupRequest {
        @Valid @JsonUnwrapped
        private CaregiverBasicInfo basicInfo;
        @NotEmpty
        private String password;
        private Boolean employmentStatus;
        private List<CertificateRequest> certificateRequestList;
        private List<ExperienceRequest> experienceRequestList;
    }

    @Getter
    public static class CaregiverUpdateRequest {
        @Valid @JsonUnwrapped
        private CaregiverBasicInfo basicInfo;
        private String img;
        private List<CertificateRequest> certificateRequestList;
        private List<ExperienceRequest> experienceRequestList;
    }

    @Getter
    public static class CertificateRequest {
        private String certNum;
        @NotNull
        private CertType certType;
        @NotNull
        private Level certRate;
    }

    @Getter
    public static class ExperienceRequest {
        @NotNull
        private int duration;
        @NotEmpty
        private String title;
        @NotEmpty
        private String description;
    }
}
