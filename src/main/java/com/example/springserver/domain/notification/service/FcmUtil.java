package com.example.springserver.domain.notification.service;

import com.example.springserver.domain.notification.dto.response.FcmDTO;
import com.example.springserver.domain.notification.entity.FcmToken;
import com.example.springserver.domain.notification.entity.UserRole;
import com.example.springserver.domain.notification.repository.FcmTokenRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FcmUtil  {
    private final FcmTokenRepository fcmTokenRepository;
    private final FirebaseMessaging firebaseMessaging;
    // 회원 한 명과 관련된 FCM 토큰에 메시지를 보내는 기능
    @Async("taskExecutor")
    public void singleFcmSend(Long userId, UserRole role, FcmDTO fcmDTO) {
        fcmTokenRepository.findByUserIdAndRole(userId, role)
                .filter(tokens -> !tokens.isEmpty()) // 빈 리스트 인지 확인
                .ifPresentOrElse(
                        tokens -> tokens.stream()
                                .map(FcmToken::getToken)
                                .map(token -> makeMessage(token, fcmDTO))
                                .forEach(this::sendMessage),
                        () -> log.error("❌ 이 회원은 FCM 토큰이 전무하네요! 오래 접속하지 않았거나, 탈퇴회원 입니다. ❌ : member_id {} ", userId)
                );
    }

    private Message makeMessage(String token, FcmDTO fcmDTO) {
        log.info(token);
        Notification.Builder notificationBuilder =
                Notification.builder()
                        .setTitle(fcmDTO.title())
                        .setBody(fcmDTO.body());

        return Message.builder()
                .setNotification(notificationBuilder.build())
                .setToken(token)
                .build();
    }

    public void sendMessage(Message message) {
        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            log.error("fcm send error");
            log.error(e.getMessage());
            e.getStackTrace();
        }
    }

    public FcmDTO makeFcmDTO(String title, String body) {
        return FcmDTO.builder()
                .title(title)
                .body(body)
                .build();
    }
}