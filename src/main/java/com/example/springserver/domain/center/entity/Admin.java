package com.example.springserver.domain.center.entity;

import com.example.springserver.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
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

    public void update(String name, String connect) {
        if (name != null) {
            this.name = name;
        }
        if (connect != null) {
            this.connect = connect;
        }
    }

    /* 양방향 연관관계 편의 메서드 */
    public void changeCenter(Center center) {
        this.center = center;
        if (!center.getAdmins().contains(this)) {
            center.getAdmins().add(this);
        }
    }
}