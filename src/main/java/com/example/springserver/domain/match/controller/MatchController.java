package com.example.springserver.domain.match.controller;

import com.example.springserver.domain.caregiver.converter.CaregiverConverter;
import com.example.springserver.domain.match.dto.request.MatchRequestDto;
import com.example.springserver.domain.match.dto.request.MatchRequestDto.RecruitReq;
import com.example.springserver.domain.match.service.MatchService;
import com.example.springserver.global.security.util.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import static com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDto.*;
import static com.example.springserver.domain.caregiver.dto.response.CaregiverResponseDto.*;

@RestController
@Validated
@Tag(name = "Match API", description = "Match 요청,추천과 조회 API")
@RequestMapping("/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @Operation(summary = "요양보호사 구인요청응답", description = "Put")
    @PutMapping("/response")
    public String responseToRecruit(@AuthenticationPrincipal CustomUserDetails user,
                                                      @RequestBody @Valid RecruitReq request){
        return matchService.responseToRecruit(user,request);
    }

    @Operation(summary = "요양보호사 매칭현황리스트 조회", description = "Get")
    @GetMapping("/matching")
    public MatchedListRes responseToRecruit(@AuthenticationPrincipal CustomUserDetails user){
        return CaregiverConverter.toMatchedListRes(matchService.getCalenderList(user));
    }

    @Operation(summary = "요양보호사 근무요청리스트 조회", description = "Get")
    @GetMapping("/requests")
    public RequestsListRes getListOfRequests(@AuthenticationPrincipal CustomUserDetails user){
        return CaregiverConverter.toRequestListRes(matchService.getRequests(user));
    }


}
