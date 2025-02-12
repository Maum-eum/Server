package com.example.springserver.controller;

import com.example.springserver.apiPayload.ApiResponse;
import com.example.springserver.converter.CaregiverConverter;
import com.example.springserver.domain.entity.Caregiver;
import com.example.springserver.dto.CaregiverDTO.CaregiverRequestDTO;
import com.example.springserver.dto.CaregiverDTO.CaregiverResponseDTO;
import com.example.springserver.service.JoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@Tag(name = "Caregiver API", description = "Caregiver에 해당하는 요양 보호사에 대한 API")
@RequestMapping("/caregiver")
public class CaregiverController {

    private final JoinService joinService;

    public CaregiverController (JoinService joinService) {
        this.joinService = joinService;
    }

    @Operation(summary = "회원가입", description = "Post")
    @PostMapping("/signup")
    public ApiResponse<CaregiverResponseDTO.SignUpCaregiverResult> signUpCaregiver(@RequestBody @Valid CaregiverRequestDTO.SignUpCaregiverReq request){

        Caregiver newCaregiver = joinService.signUpCaregiver(request);

        return ApiResponse.onSuccess(CaregiverConverter.toSignUpCaregiverResult(newCaregiver));
    }
}
