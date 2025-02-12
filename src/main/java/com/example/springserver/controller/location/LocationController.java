package com.example.springserver.controller.location;

import com.example.springserver.apiPayload.ApiResponse;
import com.example.springserver.converter.location.LocationConverter;
import com.example.springserver.domain.entity.location.Location;
import com.example.springserver.domain.entity.location.Sido;
import com.example.springserver.domain.entity.location.Sigungu;
import com.example.springserver.service.location.LocationService;
import com.example.springserver.service.location.SidoService;
import com.example.springserver.service.location.SigunguService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.springserver.dto.Location.LocationResponseDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/location")
public class LocationController {

    private final LocationService locationService;
    private final SidoService sidoService;
    private final SigunguService sigunguService;

    // 전체 시,도 조회
    @GetMapping("/sidoList")
    public ApiResponse<List<ResponseSidoDto>> getSido() {
        List<Sido> sidoList = sidoService.getSidoList();
        return ApiResponse.onSuccess(LocationConverter.toSidoListDto(sidoList));
    }

    // 주어진 시,도 내 시군구 지역 조회
    @GetMapping("/sigunguList")
    public ApiResponse<List<ResponseSigunguDto>> getSigungu(@RequestParam Long sidoId) {
        List<Sigungu> sigunguList = sigunguService.getSigunguList(sidoId);
        return ApiResponse.onSuccess(LocationConverter.toSigunguListDto(sigunguList));
    }

    // 주어진 시,군,구 내 읍면동 Location 조회
    @GetMapping("/locationList")
    public ApiResponse<List<ResponseLocationDto>> getLocation(@RequestParam Long sigunguId) {
        List<Location> locationList = locationService.getLocationList(sigunguId);
        return ApiResponse.onSuccess(LocationConverter.toLocationListDto(locationList));
    }


}
