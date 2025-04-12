package com.example.springserver.domain.center.service;

import com.example.springserver.domain.center.converter.ElderConverter;
import com.example.springserver.domain.center.dto.request.ElderRequestDto.CreateRequestDto;
import com.example.springserver.domain.center.dto.request.ElderRequestDto.RequestDto;
import com.example.springserver.domain.center.entity.Center;
import com.example.springserver.domain.center.entity.Elder;
import com.example.springserver.domain.center.mapper.ElderMapper;
import com.example.springserver.domain.center.repository.CenterRepository;
import com.example.springserver.domain.center.repository.ElderRepository;
import com.example.springserver.global.apiPayload.format.CenterException;
import com.example.springserver.global.apiPayload.format.ElderException;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ElderService {

    private final ElderRepository elderRepository;
    private final CenterRepository centerRepository;
    private final S3Service s3Service;
    private final ElderMapper elderMapper;

    @Transactional
    public Elder createElder(Long centerId, CreateRequestDto createDto, MultipartFile profileImg) {
        log.info("isTemporary 값 확인: {}", createDto.isTemporarySave()); // 로그 추가
        Center validCenter = getValidCenter(centerId);

        // 이미지 적용
        String imgUrl = saveImgUrl(profileImg);

        Elder createdElder = elderRepository.save(ElderConverter.toSaveElder(createDto, validCenter, imgUrl));
        createdElder.changeCenter(validCenter);

        return createdElder;
    }

    public List<Elder> getElderList(Long centerId) {
        getValidCenter(centerId);
        return elderRepository.findByCenter_CenterId(centerId);
    }

    public Elder getElderDetail(Long centerId, Long elderId) {
        return getValidElder(elderId, centerId);
    }

    @Transactional
    public void updateElder(Long centerId, Long elderId, RequestDto updateRequestDto, MultipartFile profileImg) {
        Elder validElder = getValidElder(elderId, centerId);

        // 이미지 적용
        String imgUrl = saveImgUrl(profileImg);
        updateRequestDto.setImgUrl(imgUrl);
        updateSelectedElderInfo(validElder, updateRequestDto);
    }

    @Transactional
    public Elder deleteElder(Long centerId, Long elderId) {

        Elder validElder = getValidElder(elderId, centerId);
        Center validCenter = getValidCenter(centerId);

        // remove
        validCenter.removeElder(validElder);
        elderRepository.delete(validElder);

        return validElder;
    }

    private void updateSelectedElderInfo(Elder elder, RequestDto updateRequestDto) {

        // 변경 요청이 들어온 필드만 수정 by.Mapper
        elderMapper.updateElderFromDto(elder, updateRequestDto);

        if (updateRequestDto.getCenterName() != null) {
            updateElderCenter(elder, updateRequestDto);
        }
    }

    private void updateElderCenter(Elder elder, RequestDto updateRequestDto) {
        Center currentCenter = elder.getCenter();

        currentCenter.getElders().remove(elder); // 기존 센터에서 제거

        Center newCenter = centerRepository.findByCenterName(updateRequestDto.getCenterName())
                .orElseThrow(() -> new CenterException(ErrorCode.CENTER_NOT_FOUND));

        elder.changeCenter(newCenter);
    }

    public Center getValidCenter(Long centerId) { // 센터 유효성 체크
        return centerRepository.findById(centerId)
                .orElseThrow(() -> new ElderException(ErrorCode.CENTER_NOT_FOUND));
    }

    public Elder getValidElder(Long elderId, Long centerId) { // 어르신 유효성 체크
        return elderRepository.findByElderIdAndCenter_CenterId(elderId, centerId)
                .orElseThrow(() -> new ElderException(ErrorCode.ELDER_NOT_BELONG_TO_CENTER));
    }

    public String saveImgUrl(MultipartFile profileImg) {
        if(profileImg == null) {
            return "http://localhost:8080/basicImg.jpeg";
        } else {
            return s3Service.uploadFileImage(profileImg);
        }
    }
}