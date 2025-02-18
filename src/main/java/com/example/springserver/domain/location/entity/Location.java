package com.example.springserver.domain.location.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id",nullable = false)
    private Long locationId;

    @NotNull
    @Column(name = "sigungu_id", nullable = false)
    private Long sigunguId;

    @Size(max = 20)
    @NotNull
    @Column(name = "dong_name",nullable = false)
    private String dongName;

    @Size(max = 20)
    @NotNull
    @Column(name = "sido_name",nullable = false)
    private String sidoName;

    @Size(max = 20)
    @NotNull
    @Column(name = "sigungu_name",nullable = false)
    private String sigunguName;

    public String getAddress(){
        return sidoName+" "+sigunguName+" "+dongName;
    }
}