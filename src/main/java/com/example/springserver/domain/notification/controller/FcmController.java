package com.example.springserver.domain.notification.controller;

import com.example.springserver.domain.notification.dto.request.FcmTestRequest;
import com.example.springserver.domain.notification.dto.request.UpdateFcmTokenRequest;
import com.example.springserver.domain.notification.service.FcmService;
import com.example.springserver.global.security.util.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/fcm")
@RequiredArgsConstructor
public class FcmController {
    private final FcmService fcmService;

    @PostMapping("/save-token")
    public void saveFcmToken( @RequestBody UpdateFcmTokenRequest request, @AuthenticationPrincipal
        CustomUserDetails user) {
        fcmService.saveFcmToken(request, user);
    }

    @PostMapping("/test-sending")
    public void testSend(@RequestBody FcmTestRequest request) {
        fcmService.testSending(request);
    }
}
