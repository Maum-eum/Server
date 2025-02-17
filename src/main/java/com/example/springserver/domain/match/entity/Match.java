package com.example.springserver.domain.match.entity;

import com.example.springserver.domain.caregiver.entity.JobCondition;
import com.example.springserver.domain.center.entity.RecruitCondition;
import com.example.springserver.domain.match.entity.enums.MatchStatus;
import com.example.springserver.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "`match`")
@Where(clause = "deleted_at IS NULL")
@Builder
public class Match extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id", nullable = false)
    private Long id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requirement_condition_id", nullable = false)
    private RecruitCondition requirementCondition;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_condition_id", nullable = false)
    private JobCondition jobCondition;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Version
    private Integer version;

}