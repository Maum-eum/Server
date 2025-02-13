package com.example.springserver.domain.caregiver.entity;

import com.example.springserver.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Entity
@Builder
@Table(name = "experience")
@NoArgsConstructor
@AllArgsConstructor
public class Experience extends BaseEntity {
    @Id
    @Column(name = "experience_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "caregiver_id", nullable = false)
    private Caregiver caregiver;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

}