package com.example.springserver.domain.entity;

import com.example.springserver.domain.entity.Center;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "admin")
public class Admin {
    @Id
    @Column(name = "admin_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "center_id", nullable = false)
    private Center center;

    @Size(max = 255)
    @NotNull
    @Column(name = "connect", nullable = false)
    private String connect;

    @Size(max = 255)
    @Column(name = "Field")
    private String field;

    @Size(max = 255)
    @Column(name = "Field2")
    private String field2;

}