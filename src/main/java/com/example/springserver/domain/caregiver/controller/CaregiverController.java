package com.example.springserver.domain.caregiver.controller;

import com.example.springserver.domain.caregiver.converter.CaregiverConverter;
import com.example.springserver.domain.caregiver.dto.response.CaregiverResponseDTO;
import com.example.springserver.domain.caregiver.entity.Caregiver;
import com.example.springserver.domain.caregiver.service.CareGiverService;
import com.example.springserver.global.security.util.CustomUserDetails;
import com.example.springserver.service.JoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDTO.*;
import static com.example.springserver.domain.caregiver.dto.response.CaregiverResponseDTO.*;

@RestController
@Validated
@Tag(name = "Caregiver API", description = "Caregiver에 해당하는 요양 보호사에 대한 API")
@RequestMapping("/caregiver")
@RequiredArgsConstructor
public class CaregiverController {

    private final JoinService joinService;
    private final CareGiverService careGiverService;

    @Operation(summary = "회원가입", description = "Post")
    @PostMapping("/signup")
    public SignUpCaregiverResult signUpCaregiver(@RequestBody @Valid SignUpCaregiverReq request){
        Caregiver newCaregiver = joinService.signUpCaregiver(request);
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
                                                    @RequestBody @Valid UpdateCaregiverReq request){
        Caregiver searched = careGiverService.updateUserInfo(user,request);
        return CaregiverConverter.infoResponseDto(searched);
    }

    @Operation(summary = "요양보호사 구인상태변경", description = "Put")
    @PutMapping("/status")
    public Boolean changeStatus(@AuthenticationPrincipal CustomUserDetails user){
        return careGiverService.changeStatus(user);
    }

    @Operation(summary = "요양보호사 구직조건등록 및 수정", description = "Post")
    @PostMapping("/jobcondition")
    public JobConditionResponseDTO createJobCondition(@AuthenticationPrincipal CustomUserDetails user,
                                                      @RequestBody @Valid JobConditionRequestDTO request){
        return  careGiverService.createOrUpdateJobCondition(user,request);
    }

    @Operation(summary = "요양보호사 구직조건수정", description = "Put")
    @PutMapping("/jobcondition")
    public JobConditionResponseDTO updateJobCondition(@AuthenticationPrincipal CustomUserDetails user,
                                                      @RequestBody @Valid JobConditionRequestDTO request){
        return  careGiverService.createOrUpdateJobCondition(user,request);
    }

    @Operation(summary = "요양보호사 구직정보조회", description = "Get")
    @GetMapping("/jobcondition")
    public JobConditionResponseDTO getJobCondition(@AuthenticationPrincipal CustomUserDetails user){
        return careGiverService.getJobCondition(user);
    }

    @Operation(summary = "요양보호사 상세정보조회", description = "Get")
    @GetMapping("/detail")
    public DetailJobConditionResponseDTO getDetailJobCondition(@AuthenticationPrincipal CustomUserDetails user){
        return careGiverService.getDetailedJobCondition(user);
    }

    @Operation(summary = "요양보호사 구인요청응답", description = "Put")
    @PutMapping("/response")
    public String responseToRecruit(@AuthenticationPrincipal CustomUserDetails user,
                                                      @RequestBody @Valid RecruitReq request){
        return careGiverService.responseToRecruit(user,request);
    }

    @Operation(summary = "요양보호사 매칭현황리스트 조회", description = "Get")
    @PutMapping("/matching")
    public List<MatchedStatus> responseToRecruit(@AuthenticationPrincipal CustomUserDetails user){
        return careGiverService.getCalenderList(user);
    }

//
//    @Operation(summary = "요양보호사 정보수정", description = "Put")
//    @PutMapping("/profile")
//    public CareGiverInfoResponseDTO updateCaregiver(@RequestBody @Valid UpdateCaregiverReq request){
//        Caregiver searched = careGiverService.updateUserInfo(request);
//        return CaregiverConverter.infoResponseDto(searched);
//    }
//
//


}
