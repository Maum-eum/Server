package com.example.springserver.domain.caregiver.entity;

import com.example.springserver.domain.location.entity.Location;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "work_location")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_location_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    private Location locationId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "job_condition_id", nullable = false)
    private JobCondition jobCondition;

}