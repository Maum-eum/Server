package com.example.springserver.controller.recruit;

import com.example.springserver.apiPayload.ApiResponse;
import com.example.springserver.converter.recruit.RecruitCondConverter;
import com.example.springserver.domain.entity.recruit.RecruitCondition;
import com.example.springserver.dto.recruitCond.RecruitCondRequestDto.CreateReqDto;
import com.example.springserver.dto.recruitCond.RecruitCondRequestDto.CreateReqTimeDto;
import com.example.springserver.dto.recruitCond.RecruitCondRequestDto.UpdateRequestDto;
import com.example.springserver.dto.recruitCond.RecruitCondResponseDto.CreateDto;
import com.example.springserver.dto.recruitCond.RecruitCondResponseDto.ResponseDto;
import com.example.springserver.service.recruit.RecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/{center_id}/recruit")
public class RecruitController {

    private final RecruitService recruitService;

    // 구인 요양보호사 조건 등록
    @PostMapping
    public ApiResponse<CreateDto> createRecruit(@PathVariable Long center_id,
                                                @RequestBody CreateReqDto createReqDto,
                                                @RequestBody CreateReqTimeDto createReqTimeDto) {

        RecruitCondition createRecruitCond = recruitService.createRecruit(center_id, createReqDto, createReqTimeDto);
        return ApiResponse.onSuccess(RecruitCondConverter.toCreateDto(createRecruitCond));
    }

    // 구인 요양보호사 조건 수정
    @PutMapping("/{recruit_condition_id}")
    public ApiResponse<ResponseDto> updateRecruitCondition(@PathVariable Long center_id,
                                                           @PathVariable Long recruit_condition_id, @RequestBody UpdateRequestDto updateRequestDto) {

        RecruitCondition updatedRecruitCondition = recruitService.updateRecruitCondition(center_id, recruit_condition_id, updateRequestDto);
        return ApiResponse.onSuccess(RecruitCondConverter.toResponseDto(updatedRecruitCondition));
    }

    // 구인 요양보호사 조건 삭제
    @DeleteMapping("/{recruit_condition_id}")
    public ApiResponse<String> deleteRecruitCondition(@PathVariable Long center_id,
                                                      @PathVariable Long recruit_condition_id) {

        return ApiResponse.onSuccess(recruitService.deleteRecruitCondition(center_id, recruit_condition_id));
    }
}
