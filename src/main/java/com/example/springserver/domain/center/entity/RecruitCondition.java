package com.example.springserver.domain.center.entity;

import com.example.springserver.domain.center.converter.RecruitConverter;
import com.example.springserver.domain.center.dto.request.RecruitRequestDto;
import com.example.springserver.domain.center.dto.request.RecruitRequestDto.RequestTimeDto;
import com.example.springserver.domain.center.dto.request.RecruitRequestDto.UpdateRequestDto;
import com.example.springserver.domain.center.entity.enums.CareType;
import com.example.springserver.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "recruit_condition")
public class RecruitCondition extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruit_condition_id", nullable = false)
    private Long recruitConditionId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "elder_id", nullable = false)
    private Elder elder;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CareType careType; // 근무 종류

    @NotNull
    @Column(nullable = false)
    private boolean flexibleSchedule; // 시간 협의 여부

    @OneToMany(mappedBy = "recruitCondition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecruitTime> recruitTimes = new ArrayList<>();

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

    public void addRecruitTime(RecruitTime recruitTime) {
        if (!this.recruitTimes.contains(recruitTime)) {
            this.recruitTimes.add(recruitTime);
            recruitTime.setRecruitCondition(this); // 양방향 관계 설정
        }
    }

    public void update(UpdateRequestDto updateRequestDto) {
        if (updateRequestDto.getCareType() != null) {
            this.careType = updateRequestDto.getCareType();
        }
        this.flexibleSchedule = updateRequestDto.isFlexibleSchedule();
        this.desiredHourlyWage = updateRequestDto.getDesiredHourlyWage();
        this.selfFeeding = updateRequestDto.isSelfFeeding();
        this.mealPreparation = updateRequestDto.isMealPreparation();
        this.cookingAssistance = updateRequestDto.isCookingAssistance();
        this.enteralNutritionSupport = updateRequestDto.isEnteralNutritionSupport();
        this.selfToileting = updateRequestDto.isSelfToileting();
        this.occasionalToiletingAssist = updateRequestDto.isOccasionalToiletingAssist();
        this.diaperCare = updateRequestDto.isDiaperCare();
        this.catheterOrStomaCare = updateRequestDto.isCatheterOrStomaCare();
        this.independentMobility = updateRequestDto.isIndependentMobility();
        this.mobilityAssist = updateRequestDto.isMobilityAssist();
        this.wheelchairAssist = updateRequestDto.isWheelchairAssist();
        this.immobile = updateRequestDto.isImmobile();
        this.cleaningLaundryAssist = updateRequestDto.isCleaningLaundryAssist();
        this.bathingAssist = updateRequestDto.isBathingAssist();
        this.hospitalAccompaniment = updateRequestDto.isHospitalAccompaniment();
        this.exerciseSupport = updateRequestDto.isExerciseSupport();
        this.emotionalSupport = updateRequestDto.isEmotionalSupport();
        this.cognitiveStimulation = updateRequestDto.isCognitiveStimulation();
    }
}