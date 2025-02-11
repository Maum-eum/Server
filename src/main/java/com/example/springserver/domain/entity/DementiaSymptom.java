package com.example.springserver.domain.entity;

import com.example.springserver.domain.entity.elder.ElderEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "dementia_symptoms_")
public class DementiaSymptom {
    @Id
    @Column(name = "dementia_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "elder_id", nullable = false)
    private ElderEntity elderEntity;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "nomal", nullable = false)
    private Boolean nomal = false;

    @Column(name = "Field")
    private Boolean field;

    @Column(name = "Field2")
    private Boolean field2;

    @Column(name = "Field3")
    private Boolean field3;

    @Column(name = "Field4")
    private Boolean field4;

    @Column(name = "Field5")
    private Boolean field5;

}