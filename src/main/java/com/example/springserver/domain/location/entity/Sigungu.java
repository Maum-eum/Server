package com.example.springserver.domain.location.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Sigungu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sigungu_id",nullable = false)
    private Long sigunguId;

    @NotNull
    @Column(name = "sido_id",nullable = false)
    private Long sidoId;

    @Size(max = 20)
    @NotNull
    @Column(name = "sigungu_name",nullable = false)
    private String sigunguName;

    @OneToMany(mappedBy = "locationId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Location> locationList = new ArrayList<>();

}