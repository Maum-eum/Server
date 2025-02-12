package com.example.springserver.controller.elder;

import com.example.springserver.apiPayload.ApiResponse;
import com.example.springserver.converter.elder.ElderConverter;
import com.example.springserver.domain.entity.elder.Elder;
import com.example.springserver.dto.elder.ElderRequestDto.CreateReqDto;
import com.example.springserver.dto.elder.ElderResponseDto.CreateDto;
import com.example.springserver.dto.elder.ElderResponseDto.ResponseDto;
import com.example.springserver.service.elder.ElderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/{center_id}/elders")
public class ElderController {

    private final ElderService elderService;

    // 센터 내 어르신 등록
    @PostMapping("/enroll")
    public ApiResponse<CreateDto> createElder(@PathVariable Long center_id, @RequestBody CreateReqDto createReqDto) {

        Elder createdElder = elderService.createElder(center_id, createReqDto);
        return ApiResponse.onSuccess(ElderConverter.toCreateDto(createdElder));
    }

    // 센터 내 어르신 목록 조회
    @GetMapping
    public ApiResponse<List<ResponseDto>> getElderList(@PathVariable Long center_id) {
        List<Elder> elderList = elderService.getElderList(center_id);
        return ApiResponse.onSuccess(ElderConverter.toListDto(elderList));
    }

    // 센터 내 어르신 상세 조회
    @GetMapping("{elder_id}")
    public ApiResponse<ResponseDto> getElderDetail(@PathVariable Long center_id, @PathVariable Long elder_id) {
        Elder elderDetail = elderService.getElderDetail(center_id, elder_id);
        return ApiResponse.onSuccess(ElderConverter.toResponseDto(elderDetail));
    }

    // 센터 내 어르신 삭제
    @DeleteMapping("{elder_id}")
    public ApiResponse<String> deleteElder(@PathVariable Long center_id, @PathVariable Long elder_id) {

        elderService.deleteElder(center_id, elder_id);
        return ApiResponse.onSuccess("삭제 완료");
    }
}
