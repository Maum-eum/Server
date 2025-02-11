package com.example.springserver.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "elder")
public class Elder {
    @Id
    @Column(name = "elder_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "center_id", nullable = false)
    private Center center;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "gender", nullable = false)
    private Integer gender;

    @NotNull
    @Column(name = "birth", nullable = false)
    private LocalDate birth;

    @Lob
    @Column(name = "rate")
    private String rate;

    @Size(max = 255)
    @Column(name = "img")
    private String img;

    @Column(name = "weight")
    private Integer weight;

    @Size(max = 255)
    @Column(name = "Field")
    private String field;

}