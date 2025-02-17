package com.example.springserver.domain.caregiver.controller;

import com.example.springserver.domain.caregiver.converter.CaregiverConverter;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDto;
import com.example.springserver.domain.caregiver.entity.Caregiver;
import com.example.springserver.domain.caregiver.service.CareGiverService;
import com.example.springserver.domain.match.dto.request.MatchRequestDto;
import com.example.springserver.domain.match.dto.request.MatchRequestDto.RecruitReq;
import com.example.springserver.global.security.util.CustomUserDetails;
import com.example.springserver.service.JoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import static com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDto.*;
import static com.example.springserver.domain.caregiver.dto.response.CaregiverResponseDto.*;

@RestController
@Validated
@Tag(name = "Caregiver API", description = "Caregiver에 해당하는 요양 보호사에 대한 API")
@RequestMapping("/caregiver")
@RequiredArgsConstructor
public class CaregiverController {

    private final JoinService joinService;
    private final CareGiverService careGiverService;

    @Operation(summary = "회원가입", description = "Post")
    @PostMapping(value = "/signup", consumes = "multipart/form-data")
    public SignUpCaregiverResult signUpCaregiver(
            @RequestPart("data") CaregiverRequestDto.SignUpCaregiverReqDto request,
            @RequestPart(value = "profileImg", required = false) MultipartFile profileImg){
        Caregiver newCaregiver = joinService.signUpCaregiver(request, profileImg);
        return CaregiverConverter.toSignUpCaregiverResult(newCaregiver);
    }

    @Operation(summary = "요양보호사 정보조회", description = "Get")
    @GetMapping("/profile")
    public CareGiverInfoResponseDTO getCaregiver(@AuthenticationPrincipal CustomUserDetails request){
        Caregiver searched = careGiverService.getUserInfo(request);
        return CaregiverConverter.infoResponseDto(searched);
    }

    @Operation(summary = "요양보호사 정보수정", description = "Put")
    @PutMapping("/profile")
    public CareGiverInfoResponseDTO updateCaregiver(@AuthenticationPrincipal CustomUserDetails user,
                                                    @RequestBody @Valid CaregiverRequestDto.UpdateCaregiverReqDto request){
        Caregiver searched = careGiverService.updateUserInfo(user,request);
        return CaregiverConverter.infoResponseDto(searched);
    }

    @Operation(summary = "요양보호사 구인상태변경", description = "Put")
    @PutMapping("/status")
    public Boolean changeStatus(@AuthenticationPrincipal CustomUserDetails user){
        return careGiverService.changeStatus(user);
    }
}
