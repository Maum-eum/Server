package com.example.springserver.domain.center.service;

import com.example.springserver.domain.center.entity.Center;
import com.example.springserver.domain.center.repository.CenterRepository;
import com.example.springserver.global.apiPayload.format.CenterException;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CenterService {

//    private final CenterSearchRepository centerSearchRepository;
    private final CenterRepository centerRepository;

    // Elastic Search - wildcard query
//    public Page<CenterDocument> searchCenters(String keyword, int page, int size) {
//        PageRequest pageRequest = PageRequest.of(page, size);
//        return centerSearchRepository.findByCenterNameContaining(keyword, pageRequest);
//    }

    public List<Center> searchCenterName(String keyword) {
        return centerRepository.findByCenterNameContaining(keyword);
    }

    public Center getCenter(String centerName) {
        return centerRepository.findByCenterName(centerName)
                .orElseThrow(() -> new CenterException(ErrorCode.CENTER_NOT_FOUND));
    }
}
