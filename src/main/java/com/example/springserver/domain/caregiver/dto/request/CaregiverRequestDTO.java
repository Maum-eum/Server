package com.example.springserver.domain.caregiver.dto.request;

import com.example.springserver.domain.caregiver.entity.enums.CertType;
import com.example.springserver.domain.caregiver.entity.enums.Level;
import com.example.springserver.domain.caregiver.entity.enums.ScheduleAvailability;
import com.example.springserver.domain.center.entity.enums.RecruitStatus;
import com.example.springserver.domain.center.entity.enums.Week;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
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

        private Long locationId;

    }


    @Getter
    @Builder
    public static class MatchStatus{

        private Long adminId;

        private Long elderId;

        private String elderName;

        private boolean mealAssistance;

        private boolean toiletAssistance;

        private boolean moveAssistance;

        private boolean dailyLivingAssistance;

        private boolean selfFeeding; // 스스로 식사 가능

        private boolean mealPreparation; // 식사 차려드리기

        private boolean cookingAssistance; // 요리 필요

        private boolean enteralNutritionSupport; // 경관식 보조

        private boolean selfToileting; // 스스로 배변 가능

        private boolean occasionalToiletingAssist; // 가끔 대소변 실수

        private boolean diaperCare; // 기저귀 케어 필요

        private boolean catheterOrStomaCare; // 유치도뇨/방광루/장루 관리

        private boolean independentMobility; // 스스로거동가능

        private boolean mobilityAssist; // 이동시 부축도움

        private boolean wheelchairAssist; // 휠체어 이동 보조

        private boolean immobile; // 거동 불가

        private boolean cleaningLaundryAssist; // 청소 빨래 보조

        private boolean bathingAssist; // 목욕 보조

        private boolean hospitalAccompaniment; // 병원 보조

        private boolean exerciseSupport; // 산책, 간단한 운동

        private boolean emotionalSupport; // 정서적 지원

        private boolean cognitiveStimulation; // 인지 자극 활동

        List<WorkTimes> times;

    }

    @Getter
    @Builder
    public static class WorkTimes{
        private Week dayOfWeek;

        private Long startTime;

        private Long endTime;
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
