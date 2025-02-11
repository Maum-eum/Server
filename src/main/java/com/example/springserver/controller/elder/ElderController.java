package com.example.springserver.controller.elder;

import com.example.springserver.apiPayload.ApiResponse;
import com.example.springserver.converter.elder.ElderConverter;
import com.example.springserver.domain.entity.elder.ElderEntity;
import com.example.springserver.dto.elder.ElderRequestDto.CreateReqDto;
import com.example.springserver.dto.elder.ElderResponseDto.CreatDto;
import com.example.springserver.service.elder.ElderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/{center_id}/elders")
public class ElderController {

    private final ElderService elderService;

    // 센터 내 어르신 등록
    @PostMapping("/enroll")
    public ApiResponse<CreatDto> createElder(@PathVariable Long center_id, @RequestBody CreateReqDto createReqDto) {

        ElderEntity createdElderEntity = elderService.createElder(center_id, createReqDto);
        return ApiResponse.onSuccess(ElderConverter.toCreateDto(createdElderEntity));
    }
}
