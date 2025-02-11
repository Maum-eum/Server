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
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Caregiver extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "caregiver_id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 40)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 40)
    private String name;

    @Column(nullable = false, length = 40)
    private String connect;

    @NotNull
    @ColumnDefault("0")
    private Boolean hasCar;

    @NotNull
    @ColumnDefault("0")
    private Boolean education;

    private String img;

    private String intro;

    private String address;

    private Boolean employmentStatus;

    @OneToMany(mappedBy = "caregiver")
    private List<Experience> experiences;

    @OneToMany(mappedBy = "caregiver")
    private List<Certificate> certificates;

    public String getRole() {
        return "ROLE_CAREGIVER";
    }
}