package com.example.springserver.domain.caregiver.entity;

import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDTO.JobConditionRequestDTO;
import com.example.springserver.domain.caregiver.entity.enums.ScheduleAvailability;
import com.example.springserver.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Setter
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
    @Enumerated(EnumType.STRING)
    @Column(name = "flexible_schedule", nullable = false)
    private ScheduleAvailability flexibleSchedule;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "desired_hourly_wage", nullable = false)
    private ScheduleAvailability desiredHourlyWage;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "self_feeding", nullable = false)
    private ScheduleAvailability selfFeeding;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "meal_preparation", nullable = false)
    private ScheduleAvailability mealPreparation;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "cooking_assistance", nullable = false)
    private ScheduleAvailability cookingAssistance;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "enteral_nutrition_support", nullable = false)
    private ScheduleAvailability enteralNutritionSupport;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "self_toileting", nullable = false)
    private ScheduleAvailability selfToileting;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "occasional_toileting_assist", nullable = false)
    private ScheduleAvailability occasionalToiletingAssist;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "diaper_care", nullable = false)
    private ScheduleAvailability diaperCare;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "catheter_or_stoma_care", nullable = false)
    private ScheduleAvailability catheterOrStomaCare;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "independent_mobility", nullable = false)
    private ScheduleAvailability independentMobility;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "mobility_assist", nullable = false)
    private ScheduleAvailability mobilityAssist;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "wheelchair_assist", nullable = false)
    private ScheduleAvailability wheelchairAssist;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "immobile", nullable = false)
    private ScheduleAvailability immobile;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "cleaning_laundry_assist", nullable = false)
    private ScheduleAvailability cleaningLaundryAssist;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "bathing_assist", nullable = false)
    private ScheduleAvailability bathingAssist;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "hospital_accompaniment", nullable = false)
    private ScheduleAvailability hospitalAccompaniment;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "exercise_support", nullable = false)
    private ScheduleAvailability exerciseSupport;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "emotional_support", nullable = false)
    private ScheduleAvailability emotionalSupport;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "cognitive_stimulation", nullable = false)
    private ScheduleAvailability cognitiveStimulation;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "caregiver_id", nullable = false)
    private Caregiver caregiver;

    @NotNull
    @OneToMany(mappedBy = "jobCondition",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<WorkTime> workTimes = new ArrayList<>();

    @NotNull
    @OneToMany(mappedBy = "jobCondition",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<WorkLocation> workLocations = new ArrayList<>();

    public void updateInfo(JobConditionRequestDTO req){
        this.flexibleSchedule = req.getFlexibleSchedule();
        this.desiredHourlyWage = req.getDesiredHourlyWage();
        this.selfFeeding = req.getSelfFeeding();
        this.mealPreparation = req.getMealPreparation();
        this.cookingAssistance = req.getCookingAssistance();
        this.enteralNutritionSupport = req.getEnteralNutritionSupport();
        this.selfToileting = req.getSelfToileting();
        this.occasionalToiletingAssist = req.getOccasionalToiletingAssist();
        this.diaperCare = req.getDiaperCare();
        this.catheterOrStomaCare = req.getCatheterOrStomaCare();
        this.independentMobility = req.getIndependentMobility();
        this.mobilityAssist = req.getMobilityAssist();
        this.wheelchairAssist = req.getWheelchairAssist();
        this.immobile = req.getImmobile();
        this.cleaningLaundryAssist = req.getCleaningLaundryAssist();
        this.bathingAssist = req.getBathingAssist();
        this.hospitalAccompaniment = req.getHospitalAccompaniment();
        this.exerciseSupport = req.getExerciseSupport();
        this.emotionalSupport = req.getEmotionalSupport();
        this.cognitiveStimulation = req.getCognitiveStimulation();
    }
}