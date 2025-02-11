package com.example.springserver.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "member")
public class Member {
    @Id
    @Column(name = "member_id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255)
    @NotNull
    @Column(name = "connect", nullable = false)
    private String connect;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "car", nullable = false)
    private Boolean car = false;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "education", nullable = false)
    private Boolean education = false;

    @Size(max = 255)
    @Column(name = "img")
    private String img;

    @Lob
    @Column(name = "intro")
    private String intro;

    @Lob
    @Column(name = "address")
    private String address;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ColumnDefault("1")
    @Column(name = "employment_status")
    private Boolean employmentStatus;

    @Size(max = 255)
    @Column(name = "id")
    private String id1;

    @Size(max = 255)
    @Column(name = "password")
    private String password;

}