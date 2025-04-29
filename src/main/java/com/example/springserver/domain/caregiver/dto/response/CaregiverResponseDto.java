package com.example.springserver.domain.caregiver.dto.response;

import com.example.springserver.domain.caregiver.dto.CaregiverBasicInfo;
import com.example.springserver.domain.caregiver.entity.enums.CertType;
import com.example.springserver.domain.caregiver.entity.enums.Level;
import com.example.springserver.domain.caregiver.entity.enums.Sexual;
import com.example.springserver.domain.center.entity.enums.CareType;
import com.example.springserver.domain.center.entity.enums.ElderRate;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class CaregiverResponseDto {

    @Builder
    @Getter
    public static class CaregiverSignupResponse {
        private Long caregiverId;
        private String createAt;
    }

    @Builder
    @Getter
    public static class CareGiverInfoResponse {
        @Valid @JsonUnwrapped
        private CaregiverBasicInfo basicInfo;
        private String img;
        private Boolean employmentStatus;
        private List<CertificateResponse> certificateResponseList;
        private List<ExperienceResponse> experienceResponseList;
    }

    @Getter
    @Builder
    public static class LocationResponse {
        private Long workLocationId;
        private String locationName;
    }

    @Getter
    @Builder
    public static class CertificateResponse {
        private String certNum;
        private CertType certType;
        private Level certRate;
    }

    @Getter
    @Builder
    public static class ExperienceResponse {
        private int duration;
        private String title;
        private String description;
    }

    @Getter
    @Builder
    public static class RequestsListResponse {
        private List<WorkResponse> list;
    }

    @Getter
    @Builder
    public static class WorkResponse {
        private Long elderId;
        private Long recruitConditionId;
        private Long centerId;
        private String centerName;
        private String imgUrl;
        private Integer desiredHourlyWage;
        private ElderRate rate;
        private Long age;
        private Sexual sexual;
        private List<CareType> careTypes;
    }

    @Getter
    @Builder
    public static class MatchCaregiverResponse {
        Long careGiverId;
        String username;
        String img;
        String contact;
    }
}