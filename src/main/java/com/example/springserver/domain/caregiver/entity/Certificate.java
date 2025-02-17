package com.example.springserver.domain.caregiver.entity;


import com.example.springserver.domain.caregiver.entity.enums.CertType;
import com.example.springserver.domain.caregiver.entity.enums.Level;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="certificate")
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certificate_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "caregiver_id", nullable = false)
    private Caregiver caregiver;


    @NotNull
    @Column(name = "cert_num")
    private String certNum;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "cert_type", nullable = false)
    private CertType certType;

    @Enumerated(EnumType.STRING)
    @Column(name = "cert_rate")
    private Level certRate;

    public Certificate(Caregiver caregiver, String certNum, CertType certType, Level certRate) {
        this.caregiver = caregiver;
        this.certNum = certNum;
        this.certType = certType;
        this.certRate = certRate;
    }
}