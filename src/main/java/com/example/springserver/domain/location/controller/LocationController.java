package com.example.springserver.domain.location.controller;


import com.example.springserver.domain.location.converter.LocationConverter;
import com.example.springserver.domain.location.entity.Location;
import com.example.springserver.domain.location.entity.Sido;
import com.example.springserver.domain.location.entity.Sigungu;
import com.example.springserver.service.location.LocationService;
import com.example.springserver.service.location.SidoService;
import com.example.springserver.service.location.SigunguService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.springserver.domain.location.dto.response.LocationResponseDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/location")
public class LocationController {

    private final LocationService locationService;
    private final SidoService sidoService;
    private final SigunguService sigunguService;

    // 전체 시,도 조회
    @GetMapping("/sidoList")
    public List<ResponseSidoDto> getSido() {
        List<Sido> sidoList = sidoService.getSidoList();
        return LocationConverter.toSidoListDto(sidoList);
    }

    // 주어진 시,도 내 시군구 지역 조회
    @GetMapping("/sigunguList")
    public List<ResponseSigunguDto> getSigungu(@RequestParam Long sidoId) {
        List<Sigungu> sigunguList = sigunguService.getSigunguList(sidoId);
        return LocationConverter.toSigunguListDto(sigunguList);
    }

    // 주어진 시,군,구 내 읍면동 Location 조회
    @GetMapping("/locationList")
    public List<ResponseLocationDto> getLocation(@RequestParam Long sigunguId) {
        List<Location> locationList = locationService.getLocationList(sigunguId);
        return LocationConverter.toLocationListDto(locationList);
    }

    @GetMapping("/address")
    public ResponseAddress getAddress(@RequestParam Long locationId) {
        return LocationConverter.toResponseAddress(locationService.getLocation(locationId));
    }


}
