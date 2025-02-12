package com.example.springserver.domain.center.entity;

import com.example.springserver.domain.center.entity.enums.CareType;
import com.example.springserver.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
    @Column(nullable = false)
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
    private List<RecruitTime> recruitTime;

    @Column
    private Integer desiredHourlyWage; // 희망 급여

    @Column
    private boolean selfFeeding; // 스스로 식사 가능

    @Column
    private boolean mealPreparation; // 식사 차려드리기

    @Column
    private boolean cookingAssistance; // 요리 필요

    @Column
    private boolean enteralNutritionSupport; // 경관식 보조

    @Column
    private boolean selfToileting; // 스스로 배변 가능

    @Column
    private boolean occasionalToiletingAssist; // 가끔 대소변 실수

    @Column
    private boolean diaperCare; // 기저귀 케어 필요

    @Column
    private boolean catheterOrStomaCare; // 유치도뇨/방광루/장루 관리

    @Column
    private boolean independentMobility; // 스스로거동가능

    @Column
    private boolean mobilityAssist; // 이동시 부축도움

    @Column
    private boolean wheelchairAssist; // 휠체어 이동 보조

    @Column
    private boolean immobile; // 거동 불가

    @Column
    private boolean cleaningLaundryAssist; // 청소 빨래 보조

    @Column
    private boolean bathingAssist; // 목욕 보조

    @Column
    private boolean hospitalAccompaniment; // 병원 보조

    @Column
    private boolean exerciseSupport; // 산책, 간단한 운동

    @Column
    private boolean emotionalSupport; // 정서적 지원

    @Column
    private boolean cognitiveStimulation; // 인지 자극 활동
}