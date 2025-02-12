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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long elderId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "center_id", nullable = false)
    private Center center;

    @Size(max = 255)
    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private Integer gender;

    @NotNull
    @Column(nullable = false)
    private LocalDate birth;

    @Lob
    @Column
    @Enumerated(EnumType.STRING)
    private ElderRate rate; // 장기 요양 등급

    @Size(max = 255)
    @Column
    private String imgUrl;

    @Column
    private Integer weight;
}