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
@Table(name = "location")
public class Location {
    @Id
    @Column(name = "location_id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "location_id2", nullable = false)
    private Long locationId2;

    @Size(max = 255)
    @ColumnDefault("읍,면,동")
    @Column(name = "dong")
    private String dong;

    @Size(max = 255)
    @Column(name = "Field")
    private String field;

    @Size(max = 255)
    @Column(name = "Field2")
    private String field2;

    @NotNull
    @Column(name = "care_id", nullable = false)
    private Integer careId;

}