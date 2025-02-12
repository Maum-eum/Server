package com.example.springserver.domain.entity;

import com.example.springserver.domain.entity.elder.Elder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "job_requirement_condition")
public class JobRequirementCondition {
    @Id
    @Column(name = "requirement_condition_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "elder_id", nullable = false)
    private Elder elder;

    @NotNull
    @ColumnDefault("방문요양")
    @Lob
    @Column(name = "care_type", nullable = false)
    private String careType;

    @NotNull
    @Column(name = "flexible_schedule", nullable = false)
    private Instant flexibleSchedule;

    @Column(name = "desired_hourly_wage")
    private Instant desiredHourlyWage;

    @Column(name = "self_feeding")
    private Instant selfFeeding;

    @Column(name = "meal_preparation")
    private Instant mealPreparation;

    @Column(name = "cooking_assistance")
    private Instant cookingAssistance;

    @Column(name = "enteral_nutrition_support")
    private Instant enteralNutritionSupport;

    @Column(name = "self_toileting")
    private Instant selfToileting;

    @Column(name = "occasional_toileting_assist")
    private Instant occasionalToiletingAssist;

    @Column(name = "diaper_care")
    private Boolean diaperCare;

    @Column(name = "catheter_or_stoma_care")
    private Boolean catheterOrStomaCare;

    @Column(name = "independent_mobility")
    private Boolean independentMobility;

    @Column(name = "mobility_assist")
    private Boolean mobilityAssist;

    @Column(name = "wheelchair_assist")
    private Boolean wheelchairAssist;

    @Column(name = "immobile")
    private Boolean immobile;

    @Column(name = "cleaning_laundry_assist")
    private Boolean cleaningLaundryAssist;

    @Column(name = "bathing_assist")
    private Boolean bathingAssist;

    @Column(name = "hospital_accompaniment")
    private Boolean hospitalAccompaniment;

    @Column(name = "exercise_support")
    private Boolean exerciseSupport;

    @Column(name = "emotional_support")
    private Boolean emotionalSupport;

    @Column(name = "cognitive_stimulation")
    private Boolean cognitiveStimulation;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

}