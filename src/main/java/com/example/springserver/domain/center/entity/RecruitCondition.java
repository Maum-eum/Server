package com.example.springserver.domain.center.entity;

import com.example.springserver.domain.center.converter.enums.CareTypeEnumListConverter;
import com.example.springserver.domain.center.dto.request.RecruitRequestDto.Request;
import com.example.springserver.domain.center.entity.enums.CareType;
import com.example.springserver.domain.location.entity.Location;
import com.example.springserver.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "recruit_condition")
public class RecruitCondition extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruit_condition_id", nullable = false)
    private Long recruitConditionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "elder_id", nullable = false)
    private Elder elder;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id", nullable = false)
    private Location recruitLocation;

    @Convert(converter = CareTypeEnumListConverter.class)
    @Column(name = "care_types", nullable = false)
    private List<CareType> careTypes = new ArrayList<>();

    @Column(nullable = false)
    private boolean flexibleSchedule; // 시간 협의 여부

    private String address;

    @OneToMany(mappedBy = "recruitCondition", cascade = CascadeType.ALL, orphanRemoval = true)
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

    public void updateRecruitTimes(List<RecruitTime> recruitTimes) {
        this.recruitTimes = recruitTimes;
    }

    public void setRecruitLocation(Location location) {
        this.recruitLocation = location;
    }

    public void update(Request request, Location location) {
        this.careTypes = request.getCareTypes();
        this.recruitLocation = location;
        this.flexibleSchedule = request.getRecruitConditionOptionInfo().isFlexibleSchedule();
        this.desiredHourlyWage = request.getRecruitConditionOptionInfo().getDesiredHourlyWage();
        this.selfFeeding = request.getRecruitConditionOptionInfo().isSelfFeeding();
        this.mealPreparation = request.getRecruitConditionOptionInfo().isMealPreparation();
        this.cookingAssistance = request.getRecruitConditionOptionInfo().isCookingAssistance();
        this.enteralNutritionSupport = request.getRecruitConditionOptionInfo().isEnteralNutritionSupport();
        this.selfToileting = request.getRecruitConditionOptionInfo().isSelfToileting();
        this.occasionalToiletingAssist = request.getRecruitConditionOptionInfo().isOccasionalToiletingAssist();
        this.diaperCare = request.getRecruitConditionOptionInfo().isDiaperCare();
        this.catheterOrStomaCare = request.getRecruitConditionOptionInfo().isCatheterOrStomaCare();
        this.independentMobility = request.getRecruitConditionOptionInfo().isIndependentMobility();
        this.mobilityAssist = request.getRecruitConditionOptionInfo().isMobilityAssist();
        this.wheelchairAssist = request.getRecruitConditionOptionInfo().isWheelchairAssist();
        this.immobile = request.getRecruitConditionOptionInfo().isImmobile();
        this.cleaningLaundryAssist = request.getRecruitConditionOptionInfo().isCleaningLaundryAssist();
        this.bathingAssist = request.getRecruitConditionOptionInfo().isBathingAssist();
        this.hospitalAccompaniment = request.getRecruitConditionOptionInfo().isHospitalAccompaniment();
        this.exerciseSupport = request.getRecruitConditionOptionInfo().isExerciseSupport();
        this.emotionalSupport = request.getRecruitConditionOptionInfo().isEmotionalSupport();
        this.cognitiveStimulation = request.getRecruitConditionOptionInfo().isCognitiveStimulation();
    }

    // mock data 생성용
    public RecruitCondition(Elder elder, Location recruitLocation, List<CareType> careTypes,
                            boolean flexibleSchedule, String address, boolean mealAssistance,
                            boolean toiletAssistance, boolean moveAssistance, boolean dailyLivingAssistance,
                            Integer desiredHourlyWage, boolean selfFeeding, boolean mealPreparation,
                            boolean cookingAssistance, boolean enteralNutritionSupport, boolean selfToileting,
                            boolean occasionalToiletingAssist, boolean diaperCare, boolean catheterOrStomaCare,
                            boolean independentMobility, boolean mobilityAssist, boolean wheelchairAssist, boolean immobile,
                            boolean cleaningLaundryAssist, boolean bathingAssist, boolean hospitalAccompaniment,
                            boolean exerciseSupport, boolean emotionalSupport, boolean cognitiveStimulation,
                            String detailRequiredService) {
        this.elder = elder;
        this.recruitLocation = recruitLocation;
        this.careTypes = careTypes;
        this.flexibleSchedule = flexibleSchedule;
        this.address = address;
        this.mealAssistance = mealAssistance;
        this.toiletAssistance = toiletAssistance;
        this.moveAssistance = moveAssistance;
        this.dailyLivingAssistance = dailyLivingAssistance;
        this.desiredHourlyWage = desiredHourlyWage;
        this.selfFeeding = selfFeeding;
        this.mealPreparation = mealPreparation;
        this.cookingAssistance = cookingAssistance;
        this.enteralNutritionSupport = enteralNutritionSupport;
        this.selfToileting = selfToileting;
        this.occasionalToiletingAssist = occasionalToiletingAssist;
        this.diaperCare = diaperCare;
        this.catheterOrStomaCare =  catheterOrStomaCare;
        this.independentMobility = independentMobility;
        this.mobilityAssist = mobilityAssist;
        this.wheelchairAssist  = wheelchairAssist;
        this.immobile = immobile;
        this.cleaningLaundryAssist = cleaningLaundryAssist;
        this.bathingAssist = bathingAssist;
        this.hospitalAccompaniment = hospitalAccompaniment;
        this.exerciseSupport = exerciseSupport;
        this.emotionalSupport = emotionalSupport;
        this.cognitiveStimulation = cognitiveStimulation;
        this.detailRequiredService = detailRequiredService;
    }

    public void addRecruitTime(RecruitTime recruitTime) {
        if (this.recruitTimes == null) {
            this.recruitTimes = new ArrayList<>();
        }
        if (!this.recruitTimes.contains(recruitTime)) {
            this.recruitTimes.add(recruitTime);
            recruitTime.setRecruitCondition(this);
        }
    }
}