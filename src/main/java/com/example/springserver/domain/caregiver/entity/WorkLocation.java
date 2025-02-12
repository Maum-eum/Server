package com.example.springserver.domain.caregiver.entity;

import com.example.springserver.domain.center.entity.JobRequirementCondition;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "work_location")
public class WorkLocation {
    @Id
    @Column(name = "work_location_id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "location_id2", nullable = false)
    private Long locationId2;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requirement_condition_id", nullable = false)
    private JobRequirementCondition requirementCondition;

}