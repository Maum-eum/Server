package com.example.springserver.domain.caregiver.entity;

import com.example.springserver.domain.caregiver.entity.enums.Week;
import com.example.springserver.domain.center.entity.RecruitCondition;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "work_time")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id", nullable = false)
    private Long id;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private Week dayOfWeek;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private Long startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private Long endTime;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "job_condition_id", nullable = false)
    private JobCondition jobCondition;
}