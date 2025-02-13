package com.example.springserver.domain.caregiver.entity;

import com.example.springserver.domain.center.entity.RecruitCondition;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "work_time")
public class WorkTime {
    @Id
    @Column(name = "schedule_id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "day_of_week", nullable = false)
    private String dayOfWeek;


    @Lob
    @Column(name = "time_slot", nullable = false)
    private String timeSlot;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requirement_condition_id", nullable = false)
    private RecruitCondition requirementCondition;

}