package com.example.springserver.domain.center.entity;

import com.example.springserver.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@Entity
@Builder
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name="admin")
public class Admin extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 40)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 40)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "center_id", nullable = false)
    private Center center;

    @NotNull
    private String connect;

    public Admin(String username, String password, String name, Center center, String connect) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.center = center;
        this.connect = connect;
    }

    /* 양방향 연관관계 편의 메서드 */
    public void changeCenter(Center center) {
        this.center = center;
        if (!center.getAdmins().contains(this)) {
            center.getAdmins().add(this);
        }
    }
}