package com.example.springserver.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
public class Sigungu {

    @Id
    @Column(nullable = false)
    private Long sigungu_id;

    @NotNull
    @Column(nullable = false)
    private Long sido_id;

    @Size(max = 20)
    @NotNull
    @Column(nullable = false)
    private String sigungu_name;

}