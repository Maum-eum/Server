package com.example.springserver.domain.caregiver.entity;

import com.example.springserver.domain.caregiver.converter.CaregiverConverter;
import com.example.springserver.domain.caregiver.dto.CaregiverBasicInfo;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDto;
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
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="care_giver")
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

    // 요양보호사 기본 프로필 정보 + 사진 업데이트
    public void updateProfile(String img, CaregiverBasicInfo basicInfo) {
        this.img = img;
        this.contact = basicInfo.getContact();
        this.car = basicInfo.getCar();
        this.education = basicInfo.getEducation();
        this.intro = basicInfo.getIntro();
        this.address = basicInfo.getAddress();
    }

    public void updateCertificates(List<CaregiverRequestDto.CertificateRequest> newCertificates) {
        List<Certificate> updated = newCertificates.stream()
                .map(dto -> CaregiverConverter.toCertificate(this, dto))
                .toList();

        certificates.clear();
        certificates.addAll(updated);
    }

    public void updateExperiences(List<CaregiverRequestDto.ExperienceRequest> newExperiences) {
        List<Experience> updated = newExperiences.stream()
                .map(dto -> CaregiverConverter.toExperience(this, dto))
                .toList();

        experiences.clear();
        experiences.addAll(updated);
    }

    public void changeEmploymentStatus(Boolean status) {
        this.employmentStatus = status;
    }

    // mock data 생성용
    public Caregiver(String username, String password, String name, String contact,
                     Boolean car, Boolean education, String img, String intro,
                     String address, Boolean employmentStatus, List<Experience> experiences,
                     List<Certificate> certificates) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.contact = contact;
        this.car = car;
        this.education = education;
        this.img = img;
        this.intro = intro;
        this.address = address;
        this.employmentStatus = employmentStatus;
        this.experiences = experiences;
        this.certificates = certificates;
    }
}