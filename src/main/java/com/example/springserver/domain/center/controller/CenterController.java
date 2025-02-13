package com.example.springserver.domain.center.controller;

import com.example.springserver.domain.center.converter.CenterConverter;
import com.example.springserver.domain.center.dto.response.CenterResponseDto;
import com.example.springserver.domain.center.dto.response.CenterResponseDto.CenterSearchDto;
import com.example.springserver.domain.center.entity.Center;
import com.example.springserver.domain.center.service.CenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/center")
public class CenterController {

    private final CenterService centerService;
    @GetMapping("/search")
    public CenterSearchDto searchCenter(@RequestParam String centerName) {

        Center center = centerService.searchCenter(centerName);
        return CenterConverter.toSearchDto(center);
    }
}
