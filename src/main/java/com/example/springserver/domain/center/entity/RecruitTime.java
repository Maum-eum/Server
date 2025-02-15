package com.example.springserver.domain.center.entity;

import com.example.springserver.domain.caregiver.entity.enums.Week;
import com.example.springserver.domain.center.dto.request.RecruitRequestDto;
import com.example.springserver.domain.center.dto.request.RecruitRequestDto.RequestTimeDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "recruit_time")
public class RecruitTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long recruitTimeId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "recruit_condition_id")
    private RecruitCondition recruitCondition;

    @ColumnDefault("'MON'")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Week dayOfWeek;

    @Column(nullable = false)
    private Long startTime;

    @Column(nullable = false)
    private Long endTime;

    public void setRecruitCondition(RecruitCondition recruitCondition) {
        this.recruitCondition = recruitCondition;
    }
}