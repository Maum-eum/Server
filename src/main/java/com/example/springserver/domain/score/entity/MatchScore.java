package com.example.springserver.domain.score.entity;

import com.example.springserver.domain.caregiver.entity.JobCondition;
import com.example.springserver.domain.center.entity.RecruitCondition;
import com.example.springserver.domain.match.entity.enums.MatchStatus;
import com.example.springserver.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.Lock;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Where(clause = "deleted_at IS NULL")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "match_score",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_job_recruit",
                columnNames = {"job_condition", "recruit_condition"}
        )
)
public class MatchScore extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_score_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recruit_condition_id", nullable = false)
    private RecruitCondition recruitCondition;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_condition_id", nullable = false)
    private JobCondition jobCondition;

    @NotNull
    @Column(name = "score")
    private Integer score;

    @NotNull
    @Column(name = "caregiver_name",length = 40)
    private String caregiverName;

    @Column(name = "caregiver_img")
    private String caregiverImg;

    @Column(name = "matching_status")
    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Version
    private Integer version;
}