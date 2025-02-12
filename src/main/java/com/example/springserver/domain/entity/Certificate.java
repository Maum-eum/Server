package com.example.springserver.domain.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "certificate")
public class Certificate {
    @Id
    @Column(name = "certificate_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "caregiver_id", nullable = false)
    private Caregiver caregiver;

    @NotNull
    @Column(name = "cert_type", nullable = false)
    private String certType;

    @Column(name = "cert_rate", nullable = false)
    private String certRate;

    // todo: 간단한 자격증 번호 Valid 필요
    private String certNum;
}