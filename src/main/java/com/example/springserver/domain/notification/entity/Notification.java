package com.example.springserver.domain.notification.entity;

import com.example.springserver.global.common.entity.Created;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification")
public class Notification extends Created {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @Column(name = "sender_id", nullable = false)
    private Long userId; // ✅ Caregiver 또는 Elder의 ID 저장

    @Column(name = "receiver_id")
    private Long recevierId;

    private String content;

    @Enumerated(EnumType.STRING)
    private NoticeType type;


}
