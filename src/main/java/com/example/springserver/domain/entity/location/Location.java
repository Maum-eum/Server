package com.example.springserver.domain.entity.location;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long location_id;

    @NotNull
    @Column(nullable = false)
    private Long sigungu_id;

    @Size(max = 20)
    @NotNull
    @Column(nullable = false)
    private String dong_name;

    @Size(max = 20)
    @NotNull
    @Column(nullable = false)
    private String sido_name;

    @Size(max = 20)
    @NotNull
    @Column(nullable = false)
    private String sigungu_name;

    public String getAddress(){
        return sido_name+" "+sigungu_name+" "+dong_name;
    }
}