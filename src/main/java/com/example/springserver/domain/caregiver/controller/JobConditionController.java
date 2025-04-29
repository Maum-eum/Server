package com.example.springserver.domain.caregiver.controller;


import com.example.springserver.domain.caregiver.dto.request.JobConditionRequestDto;
import com.example.springserver.domain.caregiver.dto.response.JobConditionResponseDto.DetailResponse;
import com.example.springserver.domain.caregiver.dto.response.JobConditionResponseDto.Response;
import com.example.springserver.domain.caregiver.service.JobConditionService;
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
@Tag(name = "JobCondition API", description = "요양 보호사의 구직근무조건 API")
@RequestMapping("/caregiver/jobcondition")
@RequiredArgsConstructor
public class JobConditionController {

    private final JobConditionService jobConditionService;

    @Operation(summary = "요양보호사 구직조건등록 및 수정", description = "Post")
    @PostMapping
    public Response createJobCondition(@AuthenticationPrincipal CustomUserDetails user,
                                       @RequestBody @Valid JobConditionRequestDto.Request request){
        return jobConditionService.createJobCondition(user,request);
    }

    @Operation(summary = "요양보호사 구직조건수정", description = "Put")
    @PutMapping
    public Response updateJobCondition(@AuthenticationPrincipal CustomUserDetails user,
                                       @RequestBody @Valid JobConditionRequestDto.Request request){
        return jobConditionService.updateJobCondition(user,request);
    }

    @Operation(summary = "요양보호사 구직정보조회", description = "Get")
    @GetMapping
    public Response getJobCondition(@AuthenticationPrincipal CustomUserDetails user){
        return jobConditionService.getJobCondition(user);
    }

    @Operation(summary = "요양보호사 상세정보조회", description = "Get")
    @GetMapping("/detail")
    public DetailResponse getDetailJobCondition(@AuthenticationPrincipal CustomUserDetails user){
        return jobConditionService.getDetailedJobCondition(user);
    }
}
