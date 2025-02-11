package com.example.springserver.domain.entity;

import com.example.springserver.domain.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Center extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "center_id", nullable = false)
    private Long id;

    @Column(name = "leader_id")
    private Long leaderId;

    @OneToMany(mappedBy = "center")
    private List<Admin> admins;

    @NotNull
    @ColumnDefault("0")
    private Boolean hasCar;

    private String rate;

    private String intro;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String address;

}