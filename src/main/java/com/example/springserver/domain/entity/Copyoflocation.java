package com.example.springserver.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "copyoflocation")
public class Copyoflocation {
    @Id
    @Column(name = "location_id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "location_id2", nullable = false)
    private Long locationId2;

    @Size(max = 255)
    @NotNull
    @Column(name = "do", nullable = false)
    private String doField;

    @Size(max = 255)
    @NotNull
    @ColumnDefault("시,군,구")
    @Column(name = "city", nullable = false)
    private String city;

}