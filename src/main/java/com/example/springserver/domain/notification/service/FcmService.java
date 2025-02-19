package com.example.springserver.domain.notification.service;

import com.example.springserver.domain.caregiver.entity.Caregiver;
import com.example.springserver.domain.caregiver.repository.CaregiverRepository;
import com.example.springserver.domain.center.entity.Admin;
import com.example.springserver.domain.center.repository.AdminRepository;
import com.example.springserver.domain.notification.dto.request.FcmTestRequest;
import com.example.springserver.domain.notification.dto.request.UpdateFcmTokenRequest;
import com.example.springserver.domain.notification.dto.response.FcmDTO;
import com.example.springserver.domain.notification.entity.FcmToken;
import com.example.springserver.domain.notification.entity.UserRole;
import com.example.springserver.domain.notification.repository.FcmTokenRepository;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import com.example.springserver.global.security.util.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {
    private final FcmTokenRepository fcmTokenRepository;
    private final AdminRepository adminRepository;
    private final CaregiverRepository caregiverRepository;
    private final FcmUtil fcmUtil;

    /**
     * ✅ 현재 로그인한 사용자의 FCM 토큰 저장
     */
    public void saveFcmToken(UpdateFcmTokenRequest request, CustomUserDetails user) {
        log.info("FCM 토큰 저장하러 유저 정보: 아이디:{} 소속:{} 역할:{}",user.getId(), user.getCenter(),  user.getRole());
        if (user.getId() == null)
            throw new GlobalException(ErrorCode.MEMBER_NOT_FOUND);

        fcmTokenRepository.findByUserIdAndRoleAndToken(user.getId(),
                UserRole.valueOf(user.getRole()),
                request.fcmToken())
            .orElseGet(() -> fcmTokenRepository.save(FcmToken.builder()
                .userId(user.getId())
                .token(request.fcmToken())
                .role(UserRole.valueOf(user.getRole()))
                .build()));
    }

    public void testSending(FcmTestRequest request) {
        if(request.receiver_role().equals("ROLE_ADMIN")){
            Admin admin = adminRepository.findById(request
                .receiver_id())
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));
            fcmUtil.singleFcmSend(request.receiver_id(), UserRole.ROLE_ADMIN,
                FcmDTO.builder()
                    .title(admin.getName()+ "님의 알림")
                    .body(request.content())
                .build());
        }else{
            Caregiver caregiver = caregiverRepository
                .findById(request.receiver_id())
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));
            fcmUtil.singleFcmSend(request.receiver_id(), UserRole.ROLE_CAREGIVER,
                FcmDTO.builder()
                    .title(caregiver.getName()+ "님의 알림")
                    .body(request.content())
                    .build());
        }
    }
}
