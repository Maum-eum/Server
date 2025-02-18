package com.example.springserver.domain.notification.repository;

import com.example.springserver.domain.notification.entity.FcmToken;
import com.example.springserver.domain.notification.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {

    // 특정 사용자(Role + ID)의 FCM 토큰 존재 여부 확인
    Optional<FcmToken> findByUserIdAndRoleAndToken(Long userId, UserRole role, String token);

    // 특정 사용자(Role + ID)의 모든 FCM 토큰 조회
    Optional<List<FcmToken>> findByUserIdAndRole(Long userId, UserRole role);

    // 특정 FCM 토큰이 존재하는지 확인
    Optional<FcmToken> findByToken(String token);
}
