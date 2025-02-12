package com.example.springserver.domain.entity;

import com.example.springserver.domain.entity.recruit.RecruitCondition;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "`match`")
public class Match {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "worker_yes")
    private Boolean workerYes;

    @Column(name = "status")
    private String status;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requirement_condition_id", nullable = false)
    private RecruitCondition requirementCondition;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requirement_condition_id2", nullable = false)
    private RecruitCondition requirementConditionId2;

}