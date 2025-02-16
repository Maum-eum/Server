package com.example.springserver.domain.center.entity;

import com.example.springserver.domain.center.entity.enums.Week;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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