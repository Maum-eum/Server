package com.example.springserver.domain.entity.recruit;

import com.example.springserver.domain.entity.recruit.RecruitCondition;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "recruit_time")
public class RecruitTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long recruitTimeId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recruit_condition_id", nullable = false)
    private RecruitCondition recruitCondition;

    @NotNull
    @ColumnDefault("MON")
    @Lob
    @Column(nullable = false)
    private String dayOfWeek;

    @NotNull
    @Column(nullable = false)
    private Instant startTime;

    @NotNull
    @Column(nullable = false)
    private Instant endTime;
}