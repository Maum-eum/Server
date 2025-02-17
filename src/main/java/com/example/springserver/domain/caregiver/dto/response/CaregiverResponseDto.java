package com.example.springserver.domain.caregiver.dto.response;

import com.example.springserver.domain.caregiver.entity.enums.CertType;
import com.example.springserver.domain.caregiver.entity.enums.Level;
import com.example.springserver.domain.caregiver.entity.enums.ScheduleAvailability;
import com.example.springserver.domain.caregiver.entity.enums.Sexual;
import com.example.springserver.domain.center.entity.enums.CareType;
import com.example.springserver.domain.center.entity.enums.ElderRate;
import com.example.springserver.domain.center.entity.enums.Week;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class CaregiverResponseDto {

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


    @Getter
    @Builder
    public static class RequestsListRes{
        private List<WorkRequest> list;
    }

    @Getter
    @Builder
    public static class WorkRequest{
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



}
