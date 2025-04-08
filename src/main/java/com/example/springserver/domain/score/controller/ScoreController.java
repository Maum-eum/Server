package com.example.springserver.domain.score.controller;

import com.example.springserver.domain.match.converter.MatchConverter;
import com.example.springserver.domain.match.dto.request.MatchRequestDto.RecruitReq;
import com.example.springserver.domain.match.dto.response.MatchResponseDto.*;
import com.example.springserver.domain.match.entity.Match;
import com.example.springserver.domain.match.service.MatchService;
import com.example.springserver.domain.score.dto.ScoreDto;
import com.example.springserver.domain.score.service.ScoreService;
import com.example.springserver.global.security.util.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@Tag(name = "Score API", description = "추천 요양보호사 점수 조회 API ")
@RequestMapping("/score")
@RequiredArgsConstructor
public class ScoreController {

    private final ScoreService scoreService;

    @Operation(summary = "어르신별 추천된 요양보호사리스트 조회", description = "Get")
    @GetMapping("/recommends/{recruit_condition_id}")
    public ScoreDto.RecommendCaregiverListDto getRecommendListByElder(@AuthenticationPrincipal CustomUserDetails user,
                                                                      @PathVariable("recruit_condition_id") Long request) {
        return scoreService.getRecommendList(request);
    }
}
