package com.example.springserver.domain.entity.location;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Sido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sido_id", nullable = false)
    private Long sidoId;

    @Size(max = 20)
    @NotNull
    @Column(nullable = false)
    private String sidoName;

    @OneToMany(mappedBy = "sido", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sigungu> sigunguList = new ArrayList<>();
}
