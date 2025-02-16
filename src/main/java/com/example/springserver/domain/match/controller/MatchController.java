package com.example.springserver.domain.match.controller;

import com.example.springserver.domain.caregiver.converter.CaregiverConverter;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDTO;
import com.example.springserver.domain.match.dto.response.MatchResponseDTO;
import com.example.springserver.domain.match.dto.response.MatchResponseDTO.CaregiverRecommendedList;
import com.example.springserver.domain.match.service.MatchService;
import com.example.springserver.global.security.util.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@Tag(name = "Match API", description = "Match에 대한 API")
@RequestMapping("/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @Operation(summary = "어르신별 요양보호사 추천 리스트", description = "Get")
    @GetMapping("/recommend/{elderId}/{recruitId}")
    public CaregiverRecommendedList recommendCaregivers (@AuthenticationPrincipal CustomUserDetails caregiver,
                                                         @PathVariable Long elderId,
                                                         @PathVariable Long recruitId){
        return matchService.getCareGiverRecommendList(elderId,recruitId);
    }

}
