package com.example.springserver.domain.center.entity;

import com.example.springserver.domain.center.converter.enums.CareTypeEnumListConverter;
import com.example.springserver.domain.center.dto.request.CareRequestDto;
import com.example.springserver.domain.center.dto.request.RecruitRequestDto.RequestDto;
import com.example.springserver.domain.center.entity.enums.CareType;
import com.example.springserver.domain.location.entity.Location;
import com.example.springserver.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "care")
public class Care extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "care_id", nullable = false)
    private Long careId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "elder_id", nullable = false)
    private Elder elder;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", nullable = false)
    private Location careLocation;

    @Convert(converter = CareTypeEnumListConverter.class)
    @Column(name = "care_types", nullable = false)
    private List<CareType> careTypes = new ArrayList<>();

    @Column(nullable = false)
    private boolean flexibleSchedule; // 시간 협의 여부

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

    public void update(CareRequestDto.RequestDto requestDto, Location location) {
        this.careTypes = requestDto.getCareTypes();
        this.flexibleSchedule = requestDto.isFlexibleSchedule();
        this.careLocation = location;
        this.desiredHourlyWage = requestDto.getDesiredHourlyWage();
        this.selfFeeding = requestDto.isSelfFeeding();
        this.mealPreparation = requestDto.isMealPreparation();
        this.cookingAssistance = requestDto.isCookingAssistance();
        this.enteralNutritionSupport = requestDto.isEnteralNutritionSupport();
        this.selfToileting = requestDto.isSelfToileting();
        this.occasionalToiletingAssist = requestDto.isOccasionalToiletingAssist();
        this.diaperCare = requestDto.isDiaperCare();
        this.catheterOrStomaCare = requestDto.isCatheterOrStomaCare();
        this.independentMobility = requestDto.isIndependentMobility();
        this.mobilityAssist = requestDto.isMobilityAssist();
        this.wheelchairAssist = requestDto.isWheelchairAssist();
        this.immobile = requestDto.isImmobile();
        this.cleaningLaundryAssist = requestDto.isCleaningLaundryAssist();
        this.bathingAssist = requestDto.isBathingAssist();
        this.hospitalAccompaniment = requestDto.isHospitalAccompaniment();
        this.exerciseSupport = requestDto.isExerciseSupport();
        this.emotionalSupport = requestDto.isEmotionalSupport();
        this.cognitiveStimulation = requestDto.isCognitiveStimulation();
    }
}