package com.example.springserver.domain.center.entity;

import com.example.springserver.global.common.entity.BaseEntity;
import com.example.springserver.domain.center.entity.enums.ElderRate;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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

    @Size(max = 255)
    private String imgUrl;

    private Integer weight;

    private boolean isTemporary;

    /* 양방향 연관관계 편의 메서드 */
    public void changeCenter(Center center) {
        this.center = center;
        if (!center.getElders().contains(this)) {
            center.getElders().add(this);
        }
    }
}