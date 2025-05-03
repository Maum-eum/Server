package com.example.springserver.domain.caregiver.entity;

import com.example.springserver.domain.location.entity.Location;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "work_location")
public class WorkLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_location_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "job_condition_id", nullable = false)
    private JobCondition jobCondition;

    public void setJobCondition(JobCondition jobCondition) {
        this.jobCondition = jobCondition;
    }

    // mock data 생성용
    public WorkLocation(Location location, JobCondition jobCondition) {
        this.location = location;
        this.jobCondition = jobCondition;
    }
}