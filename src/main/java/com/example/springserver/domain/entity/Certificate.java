package com.example.springserver.domain.entity;


import com.example.springserver.domain.entity.Member;
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
    @Column(name = "cert_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @NotNull
    @Lob
    @Column(name = "cert_type", nullable = false)
    private String certType;

    @Size(max = 255)
    @Column(name = "cert_num")
    private String certNum;

    @NotNull
    @Lob
    @Column(name = "cert_rate", nullable = false)
    private String certRate;

}