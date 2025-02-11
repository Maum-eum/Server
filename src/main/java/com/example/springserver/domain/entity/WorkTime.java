package com.example.springserver.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "work_time")
public class WorkTime {
    @Id
    @Column(name = "schedule_id", nullable = false)
    private Long id;

    @NotNull
    @ColumnDefault("MON")
    @Lob
    @Column(name = "day_of_week", nullable = false)
    private String dayOfWeek;

    @NotNull
    @ColumnDefault("MORN")
    @Lob
    @Column(name = "time_slot", nullable = false)
    private String timeSlot;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requirement_condition_id", nullable = false)
    private JobRequirementCondition requirementCondition;

}