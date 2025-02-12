package com.example.springserver.domain.entity.location;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Sigungu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long sigungu_id;

    @NotNull
    @Column(nullable = false)
    private Long sido_id;

    @Size(max = 20)
    @NotNull
    @Column(nullable = false)
    private String sigungu_name;

    @OneToMany(mappedBy = "sido", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Location> locationList = new ArrayList<>();

}