package com.example.springserver.domain.caregiver.dto.response;

import com.example.springserver.domain.caregiver.dto.JobConditionOptionInfo;
import com.example.springserver.domain.caregiver.entity.enums.CertType;
import com.example.springserver.domain.caregiver.entity.enums.Level;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class JobConditionResponseDto {

    @Getter
    @Builder
    public static class Response {
        private Long jobConditionId;
        @JsonUnwrapped
        private JobConditionOptionInfo jobConditionOptionInfo;
        private List<LocationResponse> locationResponseList;
        private Long caregiverId;
    }

    @Getter
    @Builder
    public static class DetailResponse {
        //CareGiver info
        private String name;
        private String contact;
        private Boolean car;
        private Boolean education;
        private String img;
        private String intro;
        private String address;
        private Boolean employmentStatus;
        private List<CertificateResponse> certificateResponseList;
        private List<ExperienceResponse> experienceResponseList;

        //jobCondition
        private Long jobConditionId;
        @JsonUnwrapped
        private JobConditionOptionInfo jobConditionBaseDto;
        private List<LocationResponse> locationResponseList;
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
}
