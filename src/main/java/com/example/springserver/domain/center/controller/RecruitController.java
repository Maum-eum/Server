package com.example.springserver.domain.center.controller;

import com.example.springserver.domain.center.dto.request.RecruitRequestDto.RequestDto;
import com.example.springserver.domain.center.dto.response.RecruitResponseDto.ResponseDto;
import com.example.springserver.domain.center.service.RecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/{center_id}/recruit/{elder_id}")
public class RecruitController {
    private final RecruitService recruitService;

    // 구인 요양보호사 조건 목록 조회
    @GetMapping
    public List<ResponseDto> getRecruitList(@PathVariable Long center_id, @PathVariable Long elder_id) {
        return recruitService.getRecruitConditionList(center_id, elder_id);
    }

    // 구인 요양보호사 조건 상세 조회
    @GetMapping("/{recruit_id}")
    public ResponseDto getRecruit(@PathVariable Long center_id, @PathVariable Long elder_id, @PathVariable Long recruit_id) {
        return recruitService.getRecruitCondition(center_id, elder_id, recruit_id);
    }

    // 구인 요양보호사 조건 등록
    @PostMapping
    public ResponseDto createRecruit(@PathVariable Long center_id, @PathVariable Long elder_id,
                                     @RequestBody RequestDto requestDto) {
        return recruitService.createRecruitCondition(center_id, elder_id, requestDto);
    }

    // 구인 요양보호사 조건 수정
    @PutMapping("/{recruit_id}")
    public void updateRecruit(@PathVariable Long center_id, @PathVariable Long elder_id, @PathVariable Long recruit_id,
                                              @RequestBody RequestDto requestDto) {
        recruitService.updateRecruitCondition(center_id, elder_id, recruit_id, requestDto);
    }

    // 구인 요양보호사 조건 삭제
    @DeleteMapping("/{recruit_id}")
    public void deleteRecruit(@PathVariable Long center_id, @PathVariable Long elder_id, @PathVariable Long recruit_id) {
        recruitService.deleteRecruitCondition(center_id, elder_id, recruit_id);
    }
}