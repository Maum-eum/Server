package com.example.springserver.domain.center.entity;

import com.example.springserver.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.util.List;

@Getter
@Entity
@Setter
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Center extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "center_id", nullable = false)
    private Long id;

    @Column(name = "leader_id")
    private Long leaderId;

    @OneToMany(mappedBy = "center")
    private List<Admin> admins;

    @OneToMany(mappedBy = "center")
    private List<Elder> elders;

    @NotNull
    private String centerName;

    @NotNull
    @ColumnDefault("1")
    private Boolean car;

    private String rate;

    private String intro;

    private String startTime;

    private String endTime;

    private String address;

}