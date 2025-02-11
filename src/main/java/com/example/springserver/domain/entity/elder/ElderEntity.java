package com.example.springserver.domain.entity.elder;

import com.example.springserver.domain.common.BaseEntity;
import com.example.springserver.domain.entity.elder.enums.ElderRate;
import com.example.springserver.dto.elder.ElderRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="elder")
public class ElderEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long elderId;

//    private Long centerId; // 추후 연관 관계 매핑 예정

    private String name;

    private int gender;

    private LocalDate birth;

    private int weight;

    @Enumerated(EnumType.STRING)
    private ElderRate rate; // 장기 요양 등급

    private String img;

}
