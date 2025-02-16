package com.example.springserver.domain.center.controller;

import com.example.springserver.domain.center.entity.CenterDocument;
import com.example.springserver.domain.center.service.CenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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
    public Page<CenterDocument> searchCenters(
            @RequestParam String keyword,
            @PageableDefault(size = 10) Pageable pageable) {

        return centerService.searchCenters(keyword, pageable.getPageNumber(), pageable.getPageSize());
    }
}
