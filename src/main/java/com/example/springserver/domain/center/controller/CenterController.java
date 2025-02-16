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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/center")
public class CenterController {

    private final CenterService centerService;

    @GetMapping("/search")
    public List<CenterSearchDto> searchCenters(
            @RequestParam String keyword) {

        List<Center> centers = centerService.searchCenterName(keyword);
        return CenterConverter.toSearchListDto(centers);
    }
}
