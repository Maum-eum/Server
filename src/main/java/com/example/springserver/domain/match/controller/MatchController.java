package com.example.springserver.domain.match.controller;

import com.example.springserver.domain.caregiver.converter.CaregiverConverter;
import com.example.springserver.domain.match.converter.MatchConverter;
import com.example.springserver.domain.match.dto.request.MatchRequestDto;
import com.example.springserver.domain.match.dto.request.MatchRequestDto.RecruitReq;
import com.example.springserver.domain.match.dto.response.MatchResponseDto;
import com.example.springserver.domain.match.dto.response.MatchResponseDto.MatchRecommendList;
import com.example.springserver.domain.match.dto.response.MatchResponseDto.MatchedListRes;
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
        return MatchConverter.toMatchedListRes(matchService.getCalenderList(user));
    }

    @Operation(summary = "요양보호사 근무요청리스트 조회", description = "Get")
    @GetMapping("/requests")
    public MatchResponseDto.RequestsListRes getListOfRequests(@AuthenticationPrincipal CustomUserDetails user){
        return MatchConverter.toRequestListRes(matchService.getRequests(user));
    }

    @Operation(summary = "어르신별 추천된 요양보호사리스트 조회", description = "Get")
    @GetMapping("/recommends/{recruit_condition_id}")
    public MatchRecommendList getRecommendListByElder(@AuthenticationPrincipal CustomUserDetails user,
                                                      @PathVariable("recruit_condition_id") Long request){
        return MatchConverter.toRecommendList(matchService.getRecommendList(request));
    }
}
