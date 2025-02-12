package com.example.springserver.domain.center.controller;

import com.example.springserver.domain.center.converter.ElderConverter;
import com.example.springserver.domain.center.entity.Elder;
import com.example.springserver.domain.center.service.ElderService;
import com.example.springserver.domain.center.dto.request.ElderRequestDto.CreateReqDto;
import com.example.springserver.domain.center.dto.response.ElderResponseDto.CreateDto;
import com.example.springserver.domain.center.dto.response.ElderResponseDto.ResponseDto;
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
    public CreateDto createElder(@PathVariable Long center_id, @RequestBody CreateReqDto createReqDto) {

        Elder createdElder = elderService.createElder(center_id, createReqDto);
        return ElderConverter.toCreateDto(createdElder);
    }

    // 센터 내 어르신 목록 조회
    @GetMapping
    public List<ResponseDto> getElderList(@PathVariable Long center_id) {
        List<Elder> elderList = elderService.getElderList(center_id);
        return ElderConverter.toListDto(elderList);
    }

    // 센터 내 어르신 상세 조회
    @GetMapping("{elder_id}")
    public ResponseDto getElderDetail(@PathVariable Long center_id, @PathVariable Long elder_id) {
        Elder elderDetail = elderService.getElderDetail(center_id, elder_id);
        return ElderConverter.toResponseDto(elderDetail);
    }

    // 센터 내 어르신 삭제
    @DeleteMapping("{elder_id}")
    public void deleteElder(@PathVariable Long center_id, @PathVariable Long elder_id) {
        elderService.deleteElder(center_id, elder_id);
    }
}
