package com.example.springserver.domain.notification.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record FcmTestRequest (
    @Schema(description = "받는 사람 아이디", example = "2")
    Long receiver_id,
    String receiver_role,
    @Schema(description = "내용물", example = "ROLE_ADMIN, ROLE_CAREGIVER 둘 중 하나 보내세요")
    String content
) {

}
