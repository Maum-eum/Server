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
@Table(name = "copyoflocation2")
public class Copyoflocation2 {
    @Id
    @Column(name = "location_id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @ColumnDefault("특별시,광역시, 시, 도")
    @Column(name = "do", nullable = false)
    private String doField;

}