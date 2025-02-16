package com.example.springserver.domain.center.entity;

import com.example.springserver.domain.center.converter.enums.CareTypeEnumListConverter;
import com.example.springserver.domain.center.converter.enums.InmateEnumListConverter;
import com.example.springserver.domain.center.entity.enums.Inmate;
import com.example.springserver.global.common.entity.BaseEntity;
import com.example.springserver.domain.center.entity.enums.ElderRate;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

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
    @Column(name = "elder_id", nullable = false)
    private Long elderId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "center_id", nullable = false)
    private Center center;

    @Size(max = 255)
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer gender;

    @Column(nullable = false)
    private LocalDate birth;

    @Enumerated(EnumType.STRING)
    private ElderRate rate; // 장기 요양 등급

    @Convert(converter = InmateEnumListConverter.class)
    @Column(name = "inmate_types")
    private List<Inmate> inmateTypes; // 동거인 여부 enum list

    @Size(max = 255)
    private String imgUrl;

    private Integer weight;

    private boolean isTemporary;

    @OneToMany(mappedBy = "elder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecruitCondition> recruitConditions = new ArrayList<>();

    /* 양방향 연관관계 편의 메서드 */
    public void changeCenter(Center center) {
        this.center = center;
        if (!center.getElders().contains(this)) {
            center.getElders().add(this);
        }
    }
}