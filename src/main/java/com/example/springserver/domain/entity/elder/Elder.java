package com.example.springserver.domain.entity.elder;

import com.example.springserver.domain.common.BaseEntity;
import com.example.springserver.domain.entity.Center;
import com.example.springserver.domain.entity.elder.enums.ElderRate;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "elder")
public class Elder extends BaseEntity {
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
    @Enumerated(EnumType.STRING)
    private ElderRate rate; // 장기 요양 등급

    @Size(max = 255)
    @Column(name = "img")
    private String img;

    @Column(name = "weight")
    private Integer weight;

    @Size(max = 255)
    @Column(name = "Field")
    private String field;

}