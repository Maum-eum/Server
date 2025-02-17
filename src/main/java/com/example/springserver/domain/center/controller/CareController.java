package com.example.springserver.domain.center.controller;

import com.example.springserver.domain.center.converter.CareConverter;
import com.example.springserver.domain.center.dto.request.CareRequestDto;
import com.example.springserver.domain.center.dto.response.CareResponseDto;
import com.example.springserver.domain.center.entity.Care;
import com.example.springserver.domain.center.service.CareService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/{center_id}/care/{elder_id}")
public class CareController {
    private final CareService careService;

    // 필요 케어 항목 조회
    @GetMapping("/{care_id}")
    public CareResponseDto.ResponseDto getRecruit(@PathVariable Long center_id, @PathVariable Long elder_id, @PathVariable Long care_id) {
        Care recruit = careService.getCareInfo(center_id, elder_id, care_id);
        return CareConverter.toConditionResponseDto(recruit);
    }

    // 필요 케어 항목 등록
    @PostMapping
    public CareResponseDto.ResponseDto createRecruit(@PathVariable Long center_id, @PathVariable Long elder_id,
                                                        @RequestBody CareRequestDto.RequestDto requestDto) {
        return careService.createCareInfo(center_id, elder_id, requestDto);
    }

    // 필요 케어 항목 수정
    @PutMapping("/{care_id}")
    public void updateRecruit(@PathVariable Long center_id, @PathVariable Long elder_id, @PathVariable Long care_id,
                              @RequestBody CareRequestDto.RequestDto requestDto) {
        careService.updateCareInfo(center_id, care_id, elder_id, requestDto);
    }

    // 필요 케어 항목 삭제
    @DeleteMapping("/{care_id}")
    public void deleteRecruit(@PathVariable Long center_id, @PathVariable Long elder_id, @PathVariable Long care_id) {
        careService.deleteCareInfo(center_id, elder_id, care_id);
    }
}