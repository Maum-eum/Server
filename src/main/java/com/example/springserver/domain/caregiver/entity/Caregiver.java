package com.example.springserver.domain.caregiver.entity;

import com.example.springserver.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@Getter
@Builder
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
    private String contact;

    @NotNull
    private Boolean car;

    @NotNull
    private Boolean education;

    private String img;

    private String intro;

    private String address;

    private Boolean employmentStatus;

    @OneToMany(mappedBy = "caregiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Experience> experiences;

    @OneToMany(mappedBy = "caregiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Certificate> certificates;

    public void setPassword(String password) {
        this.password = password;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setCar(Boolean car) {
        this.car = car;
    }

    public void setEducation(Boolean education) {
        this.education = education;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmploymentStatus(Boolean status) {
        this.employmentStatus = status;
    }

    public String getRole() {
        return "ROLE_CAREGIVER";
    }
}