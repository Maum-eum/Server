package com.example.springserver.domain.caregiver.entity;

import com.example.springserver.domain.caregiver.dto.request.JobConditionRequestDto.JobConditionReqDto;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "job_condition")
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
    @Column(name = "desired_hourly_wage", nullable = false)
    private Integer desiredHourlyWage;

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
    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private Long startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private Long endTime;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "caregiver", nullable = false)
    private Caregiver caregiver;

    @NotNull
    @OneToMany(mappedBy = "jobCondition",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<WorkLocation> workLocations = new ArrayList<>();

    public JobCondition(ScheduleAvailability flexibleSchedule, Integer desiredHourlyWage, ScheduleAvailability selfFeeding,
                        ScheduleAvailability mealPreparation, ScheduleAvailability cookingAssistance, ScheduleAvailability enteralNutritionSupport,
                        ScheduleAvailability selfToileting, ScheduleAvailability occasionalToiletingAssist, ScheduleAvailability diaperCare,
                        ScheduleAvailability catheterOrStomaCare, ScheduleAvailability independentMobility, ScheduleAvailability mobilityAssist,
                        ScheduleAvailability wheelchairAssist, ScheduleAvailability immobile, ScheduleAvailability cleaningLaundryAssist,
                        ScheduleAvailability bathingAssist, ScheduleAvailability hospitalAccompaniment, ScheduleAvailability exerciseSupport,
                        ScheduleAvailability emotionalSupport, ScheduleAvailability cognitiveStimulation,
                        Integer dayOfWeek, Long startTime, Long endTime, Caregiver caregiver) {
        this.flexibleSchedule = flexibleSchedule;
        this.desiredHourlyWage = desiredHourlyWage;
        this.selfFeeding = selfFeeding;
        this.mealPreparation = mealPreparation;
        this.cookingAssistance = cookingAssistance;
        this.enteralNutritionSupport = enteralNutritionSupport;
        this.selfToileting = selfToileting;
        this.occasionalToiletingAssist = occasionalToiletingAssist;
        this.diaperCare = diaperCare;
        this.catheterOrStomaCare = catheterOrStomaCare;
        this.independentMobility = independentMobility;
        this.mobilityAssist = mobilityAssist;
        this.wheelchairAssist = wheelchairAssist;
        this.immobile = immobile;
        this.cleaningLaundryAssist = cleaningLaundryAssist;
        this.bathingAssist = bathingAssist;
        this.hospitalAccompaniment = hospitalAccompaniment;
        this.exerciseSupport = exerciseSupport;
        this.emotionalSupport = emotionalSupport;
        this.cognitiveStimulation = cognitiveStimulation;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.caregiver = caregiver;
    }

    public void updateInfo(JobConditionReqDto req){
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

    public void addWokLocation(WorkLocation wokLocation) {
        if (!this.workLocations.contains(wokLocation)) {
            this.workLocations.add(wokLocation);
            wokLocation.setJobCondition(this); // 양방향 관계 설정
        }
    }
}