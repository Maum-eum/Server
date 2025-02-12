package com.example.springserver.service.elder;

import com.example.springserver.apiPayload.code.status.ErrorStatus;
import com.example.springserver.apiPayload.exception.elder.ElderException;
import com.example.springserver.converter.elder.ElderConverter;
import com.example.springserver.domain.entity.elder.Elder;
import com.example.springserver.dto.elder.ElderRequestDto.CreateReqDto;
import com.example.springserver.repository.center.CenterRepository;
import com.example.springserver.repository.elder.ElderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ElderService {

    private final ElderRepository elderRepository;
    private final CenterRepository centerRepository;

    @Transactional
    public Elder createElder(Long centerId, CreateReqDto createDto) {
        isValidCenter(centerId);
        return elderRepository.save(ElderConverter.toElder(createDto));
    }

    public List<Elder> getElderList(Long centerId) {
        isValidCenter(centerId);
        return elderRepository.findByCenterId(centerId);
    }

    public Elder getElderDetail(Long centerId, Long elderId) {
        isValidCenter(centerId);
        isValidElder(centerId);
        return elderRepository.findByCenterIdAndElderId(centerId, elderId);
    }

    @Transactional
    public void deleteElder(Long centerId, Long elderId) {
        Elder elder = isValidElder(elderId);

        // 센터에 속하지 않는 어르신 삭제 방지
        if (!elder.getCenter().getId().equals(centerId))
            throw new ElderException(ErrorStatus.ELDER_NOT_BELONG_TO_CENTER);

        elderRepository.delete(elder);
    }

    public void isValidCenter(Long centerId) { // 센터 유효성 체크
        centerRepository.findById(centerId)
                .orElseThrow(() -> new ElderException(ErrorStatus.CENTER_NOT_FOUND));
    }

    public Elder isValidElder(Long elderId) { // 어르신 유효성 체크
        return elderRepository.findById(elderId)
                .orElseThrow(() -> new ElderException(ErrorStatus.ELDER_NOT_FOUND));
    }
}
