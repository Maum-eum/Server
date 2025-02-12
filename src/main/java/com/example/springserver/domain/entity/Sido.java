package com.example.springserver.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Sido {

    @Id
    @NotNull
    @Column(nullable = false)
    private Long sido_id;

    @Size(max = 20)
    @NotNull
    @Column(nullable = false)
    private String sido_name;

}