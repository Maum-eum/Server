package com.example.springserver.domain.caregiver.dto.response;

import com.example.springserver.domain.caregiver.entity.enums.CertType;
import com.example.springserver.domain.caregiver.entity.enums.Level;
import com.example.springserver.domain.caregiver.entity.enums.ScheduleAvailability;
import com.example.springserver.domain.caregiver.entity.enums.Sexual;
import com.example.springserver.domain.center.entity.RecruitTime;
import com.example.springserver.domain.center.entity.enums.CareType;
import com.example.springserver.domain.center.entity.enums.ElderRate;
import com.example.springserver.domain.center.entity.enums.Week;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

        private List<LocationResponseDTO> locationRequestDTOList;
    }

    @Getter
    @Builder
    public static class DetailJobConditionResponseDTO {

        //CareGiver info

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

        //jobCondition

        private Long jobConditionId;

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

        private List<LocationResponseDTO> locationRequestDTOList;

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
    public static class MatchedListRes{
        List<MatchedStatus> list;
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

    @Getter
    @Builder
    public static class MatchedStatus {

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

}
