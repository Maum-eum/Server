package com.example.springserver.domain.caregiver.controller;

import com.example.springserver.domain.caregiver.converter.CaregiverConverter;
import com.example.springserver.domain.caregiver.entity.Caregiver;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDTO;
import com.example.springserver.domain.caregiver.dto.response.CaregiverResponseDTO;
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
    public CaregiverResponseDTO.SignUpCaregiverResult signUpCaregiver(@RequestBody @Valid CaregiverRequestDTO.SignUpCaregiverReq request){

        Caregiver newCaregiver = joinService.signUpCaregiver(request);

        return CaregiverConverter.toSignUpCaregiverResult(newCaregiver);
    }
}
