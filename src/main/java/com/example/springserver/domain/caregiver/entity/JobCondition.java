package com.example.springserver.domain.caregiver.entity;

import com.example.springserver.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "job_condition")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobCondition extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_condition_id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "flexible_schedule", nullable = false)
    private Boolean flexibleSchedule;

    @NotNull
    @Column(name = "desired_hourly_wage", nullable = false)
    private Boolean desiredHourlyWage = false;

    @NotNull
    @Column(name = "self_feeding", nullable = false)
    private Boolean selfFeeding = false;

    @NotNull
    @Column(name = "meal_preparation", nullable = false)
    private Boolean mealPreparation = false;

    @NotNull
    @Column(name = "cooking_assistance", nullable = false)
    private Boolean cookingAssistance = false;

    @NotNull
    @Column(name = "enteral_nutrition_support", nullable = false)
    private Boolean enteralNutritionSupport = false;

    @NotNull
    @Column(name = "self_toileting", nullable = false)
    private Boolean selfToileting = false;

    @NotNull
    @Column(name = "occasional_toileting_assist", nullable = false)
    private Boolean occasionalToiletingAssist = false;

    @NotNull
    @Column(name = "diaper_care", nullable = false)
    private Boolean diaperCare = false;

    @NotNull
    @Column(name = "catheter_or_stoma_care", nullable = false)
    private Boolean catheterOrStomaCare = false;

    @NotNull
    @Column(name = "independent_mobility", nullable = false)
    private Boolean independentMobility = false;

    @NotNull
    @Column(name = "mobility_assist", nullable = false)
    private Boolean mobilityAssist = false;

    @NotNull
    @Column(name = "wheelchair_assist", nullable = false)
    private Boolean wheelchairAssist = false;

    @NotNull
    @Column(name = "immobile", nullable = false)
    private Boolean immobile = false;

    @NotNull
    @Column(name = "cleaning_laundry_assist", nullable = false)
    private Boolean cleaningLaundryAssist = false;

    @NotNull
    @Column(name = "bathing_assist", nullable = false)
    private Boolean bathingAssist = false;

    @NotNull
    @Column(name = "hospital_accompaniment", nullable = false)
    private Boolean hospitalAccompaniment = false;

    @NotNull
    @Column(name = "exercise_support", nullable = false)
    private Boolean exerciseSupport = false;

    @NotNull
    @Column(name = "emotional_support", nullable = false)
    private Boolean emotionalSupport = false;

    @NotNull
    @Column(name = "cognitive_stimulation", nullable = false)
    private Boolean cognitiveStimulation = false;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "caregiver_id", nullable = false)
    private Caregiver caregiver;

    @NotNull
    @OneToMany(mappedBy = "jobCondition",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<WorkTime> workTimes;

    @NotNull
    @OneToMany(mappedBy = "jobCondition",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<WorkLocation> workLocations;
}