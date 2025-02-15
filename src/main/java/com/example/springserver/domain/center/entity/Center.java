package com.example.springserver.domain.center.entity;

import com.example.springserver.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Center extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="center_id", nullable = false)
    private Long centerId;

    @Column(nullable = false)
    private String centerLeaderName;

    @OneToMany(mappedBy = "center", cascade = CascadeType.ALL)
    private List<Admin> admins;

    @OneToMany(mappedBy = "center", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Elder> elders = new ArrayList<>();

    @Column(nullable = false)
    private String centerName;

    @Column(nullable = false)
    private Boolean hasBathCar; // 목욕차량 보유 여부

    private String rate;

    private String intro;

    private String startTime;

    private String endTime;

    private String address;
}