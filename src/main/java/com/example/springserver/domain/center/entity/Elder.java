package com.example.springserver.domain.center.entity;

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

    private boolean isTemporarySave; // 임시 저장 여부

    @Column(nullable = false)
    private boolean isNormal; // 증상 보유 여부

    private boolean hasShortTermMemoryLoss; // 단기 기억 장애 여부

    private boolean wandersOutside; // 집밖을 배회 여부

    private boolean actsLikeChild; // 어린아이 같은 행동 여부

    private boolean hasDelusions; // 망상 여부

    private boolean hasAggressiveBehavior; // 공격적인 행동 여부


    @OneToMany(mappedBy = "elder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecruitCondition> recruitConditions = new ArrayList<>();

    @OneToOne(mappedBy = "elder", cascade = CascadeType.ALL, orphanRemoval = true)
    private Care careInfo;

    public Elder(Center center, String name, Integer gender, LocalDate birth, ElderRate rate,
                 List<Inmate> inmateTypes, String imgUrl, Integer weight,
                 boolean isTemporarySave, boolean isNormal, boolean hasShortTermMemoryLoss, boolean wandersOutside,
                 boolean actsLikeChild, boolean hasDelusions, boolean hasAggressiveBehavior) {
        this.center = center;
        this.name = name;
        this.gender = gender;
        this.birth = birth;
        this.rate = rate;
        this.inmateTypes = inmateTypes;
        this.imgUrl = imgUrl;
        this.weight = weight;
        this.isTemporarySave = isTemporarySave;
        this.isNormal = isNormal;
        this.hasShortTermMemoryLoss = hasShortTermMemoryLoss;
        this.wandersOutside = wandersOutside;
        this.actsLikeChild = actsLikeChild;
        this.hasDelusions = hasDelusions;
        this.hasAggressiveBehavior = hasAggressiveBehavior;
    }

    /* 양방향 연관관계 편의 메서드 */
    public void changeCenter(Center center) {
        this.center = center;
        if (!center.getElders().contains(this)) {
            center.getElders().add(this);
        }
    }
}