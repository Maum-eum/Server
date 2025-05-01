package com.example.springserver.domain.match.dto.response;

import com.example.springserver.domain.caregiver.dto.response.CaregiverResponseDto;
import com.example.springserver.domain.caregiver.dto.response.CaregiverResponseDto.MatchCaregiverResponse;
import com.example.springserver.domain.caregiver.dto.response.JobConditionResponseDto;
import com.example.springserver.domain.caregiver.entity.Caregiver;
import com.example.springserver.domain.caregiver.entity.JobCondition;
import com.example.springserver.domain.caregiver.entity.enums.ScheduleAvailability;
import com.example.springserver.domain.caregiver.entity.enums.Sexual;
import com.example.springserver.domain.center.dto.response.ElderResponseDto;
import com.example.springserver.domain.center.dto.response.ElderResponseDto.MatchElderResponseDto;
import com.example.springserver.domain.center.dto.response.RecruitResponseDto.Response;
import com.example.springserver.domain.center.entity.Elder;
import com.example.springserver.domain.center.entity.RecruitCondition;
import com.example.springserver.domain.center.entity.RecruitTime;
import com.example.springserver.domain.center.entity.enums.CareType;
import com.example.springserver.domain.center.entity.enums.ElderRate;
import com.example.springserver.domain.center.entity.enums.Inmate;
import com.example.springserver.domain.center.entity.enums.Week;
import com.example.springserver.domain.location.entity.Location;
import com.example.springserver.domain.match.entity.enums.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MatchResponseDto {


    @Getter
    @Builder
    public static class GeneralMatchResponse{
        private String msg;
    }

    @Getter
    @Builder
    public static class MatchRecommendList{

        private List<MatchedCaregiver> list;
    }

    @Getter
    @Builder
    public static class MatchedCaregiver{

        private Long jobConditionId;

        private Integer score;

        private String imgUrl;

        private String caregiverName;

        private MatchStatus matchStatus;
    }

    @Getter
    @Builder
    public static class MatchedListRes{

        private List<MatchedStatus> list;
    }

    @Getter
    @Builder
    public static class RequestsListRes{

        private List<WorkRequest> list;
    }

    @Getter
    @Builder
    public static class WorkRequest{

        private Long matchId;

        private Long elderId;

        private Long recruitConditionId;

        private Long centerId;

        private String centerName;

        private String imgUrl;

        private Integer desiredHourlyWage;

        private ElderRate rate;

        private Long age;

        private Sexual sexual;

        private MatchStatus status;

        private List<CareType> careTypes;

    }

    @Getter
    @Builder
    public static class MatchedStatus {

        private Long matchId;

        private Long elderId;

        private Long centerId;

        private Long recruitId;

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
    @Builder
    public static class MatchCreateDto{

        private String msg;
    }

    @Getter
    @Builder
    public static class CareGiverInfo{

        private String adminContact;

        private CaregiverResponseDto.CareGiverInfoResponse careGiverInfo;

        private ElderResponseDto.ResponseDto elderInfoDto;

        private JobConditionResponseDto.Response jobCondRes;

        private Response recruitCondRes;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CareGiverInfoDto{

        private String name;
        private String contact;
        private Boolean car;
        private Boolean education;
        private String img;
        private String intro;
        private String address;
        private Boolean employmentStatus;

        public static CareGiverInfoDto from(Caregiver caregiver) {
            return CareGiverInfoDto.builder()
                    .name(caregiver.getName())
                    .contact(caregiver.getContact())
                    .car(caregiver.getCar())
                    .education(caregiver.getEducation())
                    .img(caregiver.getImg())
                    .intro(caregiver.getIntro())
                    .address(caregiver.getAddress())
                    .employmentStatus(caregiver.getEmploymentStatus())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ElderInfoDto{

        private Long elderId;
        private String name;
        private String centerName;
        private int gender;
        private LocalDate birth;
        private List<Inmate> inmateTypes;
        private ElderRate rate;
        private String img;
        private Integer weight;
        private boolean isNormal; // 증상 보유 여부
        private boolean hasShortTermMemoryLoss; // 단기 기억 장애 여부
        private boolean wandersOutside; // 집밖을 배회 여부
        private boolean actsLikeChild; // 어린아이 같은 행동 여부
        private boolean hasDelusions; // 망상 여부
        private boolean hasAggressiveBehavior; // 공격적인 행동 여부

        public static ElderInfoDto from(Elder elder) {
            return ElderInfoDto.builder()
                    .elderId(elder.getElderId())
                    .name(elder.getName())
                    .centerName(elder.getCenter() != null ? elder.getCenter().getCenterName() : null) // Center가 null이면 NPE 방지
                    .gender(elder.getGender())
                    .birth(elder.getBirth())
                    .inmateTypes(elder.getInmateTypes()) // 필요시 강제 초기화 가능
                    .rate(elder.getRate()) // 필요시 강제 초기화 가능
                    .img(elder.getImgUrl())
                    .weight(elder.getWeight())
                    .isNormal(elder.isNormal())
                    .hasShortTermMemoryLoss(elder.isHasShortTermMemoryLoss())
                    .wandersOutside(elder.isWandersOutside())
                    .actsLikeChild(elder.isActsLikeChild())
                    .hasDelusions(elder.isHasDelusions())
                    .hasAggressiveBehavior(elder.isHasAggressiveBehavior())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JobCondRes{
        private Long id;
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
        private Integer dayOfWeek;
        private Long startTime;
        private Long endTime;

        public static JobCondRes from(JobCondition jobCondition) {
            return JobCondRes.builder()
                    .id(jobCondition.getId())
                    .flexibleSchedule(jobCondition.getFlexibleSchedule())
                    .desiredHourlyWage(jobCondition.getDesiredHourlyWage())
                    .selfFeeding(jobCondition.getSelfFeeding())
                    .mealPreparation(jobCondition.getMealPreparation())
                    .cookingAssistance(jobCondition.getCookingAssistance())
                    .enteralNutritionSupport(jobCondition.getEnteralNutritionSupport())
                    .selfToileting(jobCondition.getSelfToileting())
                    .occasionalToiletingAssist(jobCondition.getOccasionalToiletingAssist())
                    .diaperCare(jobCondition.getDiaperCare())
                    .catheterOrStomaCare(jobCondition.getCatheterOrStomaCare())
                    .independentMobility(jobCondition.getIndependentMobility())
                    .mobilityAssist(jobCondition.getMobilityAssist())
                    .wheelchairAssist(jobCondition.getWheelchairAssist())
                    .immobile(jobCondition.getImmobile())
                    .cleaningLaundryAssist(jobCondition.getCleaningLaundryAssist())
                    .bathingAssist(jobCondition.getBathingAssist())
                    .hospitalAccompaniment(jobCondition.getHospitalAccompaniment())
                    .exerciseSupport(jobCondition.getExerciseSupport())
                    .emotionalSupport(jobCondition.getEmotionalSupport())
                    .cognitiveStimulation(jobCondition.getCognitiveStimulation())
                    .dayOfWeek(jobCondition.getDayOfWeek())
                    .startTime(jobCondition.getStartTime())
                    .endTime(jobCondition.getEndTime())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecruitCondRes{
        private Long recruitConditionId;
        private Location recruitLocation;
        private String address;
        private List<CareType> careTypes = new ArrayList<>();
        private boolean flexibleSchedule; // 시간 협의 여부
        private List<RecruitTime> recruitTimes = new ArrayList<>();
        private boolean mealAssistance;
        private boolean toiletAssistance;
        private boolean moveAssistance;
        private boolean dailyLivingAssistance;
        private Integer desiredHourlyWage; // 희망 급여
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
        private String detailRequiredService;

        public static RecruitCondRes from(RecruitCondition recruitCondition) {
            if (recruitCondition == null) {
                return null;
            }

            // 🔹 Hibernate Lazy Loading 문제 방지
            Hibernate.initialize(recruitCondition.getRecruitLocation());
            Hibernate.initialize(recruitCondition.getCareTypes());
            Hibernate.initialize(recruitCondition.getRecruitTimes());

            return RecruitCondRes.builder()
                    .recruitConditionId(recruitCondition.getRecruitConditionId())
                    .address(recruitCondition.getAddress())
                    .recruitLocation(recruitCondition.getRecruitLocation())
                    .careTypes(new ArrayList<>(recruitCondition.getCareTypes()))
                    .recruitTimes(new ArrayList<>(recruitCondition.getRecruitTimes()))
                    .flexibleSchedule(recruitCondition.isFlexibleSchedule())
                    .mealAssistance(recruitCondition.isMealAssistance())
                    .toiletAssistance(recruitCondition.isToiletAssistance())
                    .moveAssistance(recruitCondition.isMoveAssistance())
                    .dailyLivingAssistance(recruitCondition.isDailyLivingAssistance())
                    .desiredHourlyWage(recruitCondition.getDesiredHourlyWage())
                    .selfFeeding(recruitCondition.isSelfFeeding())
                    .mealPreparation(recruitCondition.isMealPreparation())
                    .cookingAssistance(recruitCondition.isCookingAssistance())
                    .enteralNutritionSupport(recruitCondition.isEnteralNutritionSupport())
                    .selfToileting(recruitCondition.isSelfToileting())
                    .occasionalToiletingAssist(recruitCondition.isOccasionalToiletingAssist())
                    .diaperCare(recruitCondition.isDiaperCare())
                    .catheterOrStomaCare(recruitCondition.isCatheterOrStomaCare())
                    .independentMobility(recruitCondition.isIndependentMobility())
                    .mobilityAssist(recruitCondition.isMobilityAssist())
                    .wheelchairAssist(recruitCondition.isWheelchairAssist())
                    .immobile(recruitCondition.isImmobile())
                    .cleaningLaundryAssist(recruitCondition.isCleaningLaundryAssist())
                    .bathingAssist(recruitCondition.isBathingAssist())
                    .hospitalAccompaniment(recruitCondition.isHospitalAccompaniment())
                    .exerciseSupport(recruitCondition.isExerciseSupport())
                    .emotionalSupport(recruitCondition.isEmotionalSupport())
                    .cognitiveStimulation(recruitCondition.isCognitiveStimulation())
                    .detailRequiredService(recruitCondition.getDetailRequiredService())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class MatchDto {

        private Long matchId;

        private MatchStatus status;

        private Response requirementCondition;

        private JobConditionResponseDto.Response jobCondition;

        private MatchElderResponseDto elderInfoDto;

        private MatchCaregiverResponse careGiverInfoDto;

        private LocalDateTime deletedAt;

        private Integer version;
    }
}
