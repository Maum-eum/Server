package com.example.springserver.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "center_")
public class Center {
    @Id
    @Column(name = "center_id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "li")
    private String li;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "leader_id", nullable = false)
    private Member leader;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "car", nullable = false)
    private Boolean car = false;

    @Size(max = 255)
    @Column(name = "rate")
    private String rate;

    @Lob
    @Column(name = "intro")
    private String intro;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Size(max = 255)
    @Column(name = "Field")
    private String field;

}