
package com.example.springserver.domain.center.controller;

import com.example.springserver.domain.center.converter.ElderConverter;
import com.example.springserver.domain.center.dto.request.ElderRequestDto.CreateRequestDto;
import com.example.springserver.domain.center.dto.request.ElderRequestDto.RequestDto;
import com.example.springserver.domain.center.dto.response.ElderResponseDto.CreateDto;
import com.example.springserver.domain.center.dto.response.ElderResponseDto.DeleteResponseDto;
import com.example.springserver.domain.center.dto.response.ElderResponseDto.ResponseDto;
import com.example.springserver.domain.center.entity.Elder;
import com.example.springserver.domain.center.entity.enums.Inmate;
import com.example.springserver.domain.center.service.ElderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/{center_id}/elders")
public class ElderController {

    private final ElderService elderService;

    // 센터 내 어르신 등록
    @Operation(summary = "센터 내 어르신 등록")
    @PostMapping(consumes = "multipart/form-data")
    public CreateDto createElder(@PathVariable Long center_id,
                                 @RequestPart("data") CreateRequestDto createRequestDto,
                                 @RequestParam(value = "inmateTypes", required = false) List<String> inmateTypes,
                                 @RequestPart(value = "profileImg", required = false) MultipartFile profileImg) {

        List<Inmate> inmateEnumList = StringTypeToEnumType(inmateTypes);
        // CreateRequestDto에 inmateTypes 설정
        createRequestDto.setInmateTypes(inmateEnumList);
        Elder createdElder = elderService.createElder(center_id, createRequestDto, false, profileImg);
        return ElderConverter.toCreateDto(createdElder);
    }

    // 센터 내 어르신 목록 조회
    @Operation(summary = "센터 내 어르신 목록 조회")
    @GetMapping
    public List<ResponseDto> getElderList(@PathVariable Long center_id) {
        List<Elder> elderList = elderService.getElderList(center_id, false);
        return ElderConverter.toListDto(elderList);
    }

    // 센터 내 어르신 상세 조회
    @Operation(summary = "센터 내 어르신 상세 조회")
    @GetMapping("/{elder_id}")
    public ResponseDto getElderDetail(@PathVariable Long center_id, @PathVariable Long elder_id) {
        Elder elderDetail = elderService.getElderDetail(center_id, elder_id, false);
        return ElderConverter.toResponseDto(elderDetail);
    }

    // 센터 내 어르신 등록 (임시저장)
    @Operation(summary = "센터 내 어르신 등록 (임시저장)")
    @PostMapping(value = "/temp", consumes = "multipart/form-data")
    public CreateDto tempCreateElder(@PathVariable Long center_id,
                                     @RequestPart("data") CreateRequestDto createRequestDto,
                                     @RequestParam(value = "inmateTypes", required = false) List<String> inmateTypes,
                                     @RequestPart(value = "profileImg", required = false) MultipartFile profileImg) {
        List<Inmate> inmateEnumList = StringTypeToEnumType(inmateTypes);
        createRequestDto.setInmateTypes(inmateEnumList);
        Elder createdElder = elderService.createElder(center_id, createRequestDto, true, profileImg);
        return ElderConverter.toCreateDto(createdElder);
    }

    // 임시 저장된 어르신 목록 조회
    @Operation(summary = "임시 저장된 어르신 목록 조회")
    @GetMapping("/temp")
    public List<ResponseDto> getTempElders(@PathVariable Long center_id) {
        List<Elder> tempElders = elderService.getElderList(center_id, true);
        return ElderConverter.toListDto(tempElders);
    }

    // 임시 저장된 어르신 상세 조회
    @Operation(summary = "임시 저장된 어르신 상세 조회")
    @GetMapping("/temp/{elder_id}")
    public ResponseDto getTempElders(@PathVariable Long center_id, @PathVariable Long elder_id) {
        Elder elderDetail = elderService.getElderDetail(center_id, elder_id, true);
        return ElderConverter.toResponseDto(elderDetail);
    }

    // 센터 내 어르신 수정
    @Operation(summary = "센터 내 어르신 수정")
    @PutMapping(value = "/{elder_id}", consumes = "multipart/form-data")
    public RequestDto updateElder(@PathVariable Long center_id, @PathVariable Long elder_id,
                                  @RequestPart("data") RequestDto updateRequestDto,
                                  @RequestPart(value = "inmateTypes", required = false) List<String> inmateTypes,
                                  @RequestPart(value = "profileImg", required = false) MultipartFile profileImg) {
        List<Inmate> inmateEnumList = StringTypeToEnumType(inmateTypes);
        updateRequestDto.setInmateTypes(inmateEnumList);
        elderService.updateElder(center_id, elder_id, updateRequestDto, profileImg);
        return updateRequestDto;
    }

    // 센터 내 어르신 삭제
    @Operation(summary = "센터 내 어르신 삭제")
    @DeleteMapping("/{elder_id}")
    public DeleteResponseDto deleteElder(@PathVariable Long center_id, @PathVariable Long elder_id) {
        Elder deletedElder = elderService.deleteElder(center_id, elder_id);
        return ElderConverter.toDeleteResponseDto(deletedElder);
    }

    public List<Inmate> StringTypeToEnumType(List<String> inmateTypes) {

        // inmateTypes 값을 List<Inmate>로 변환
        return inmateTypes.stream()
                .map(String::trim) // 공백 제거
                .map(Inmate::valueOf) // Inmate Enum으로 변환
                .collect(Collectors.toList());
    }
}