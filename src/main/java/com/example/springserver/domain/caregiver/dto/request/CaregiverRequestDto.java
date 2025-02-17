package com.example.springserver.domain.caregiver.dto.request;

import com.example.springserver.domain.caregiver.entity.enums.CertType;
import com.example.springserver.domain.caregiver.entity.enums.Level;
import com.example.springserver.domain.caregiver.entity.enums.ScheduleAvailability;
import com.example.springserver.domain.center.entity.enums.RecruitStatus;
import com.example.springserver.domain.center.entity.enums.Week;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class CaregiverRequestDto {

    @Getter
    public static class SignUpCaregiverReqDto {

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

        private String intro;

        private String address;

        private Boolean employmentStatus;

        private List<CertificateRequestDTO> certificateRequestDTOList;

        private List<ExperienceRequestDTO> experienceRequestDTOList;

        public SignUpCaregiverReqDto() {
            this.car = false; // 기본값 false
            this.education = false;   // 기본값 false
        }
    }

    @Getter
    public static class UpdateCaregiverReqDto {

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
