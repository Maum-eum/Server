package com.example.springserver.domain.center.service;

import com.example.springserver.domain.center.entity.Center;
import com.example.springserver.domain.center.entity.CenterDocument;
import com.example.springserver.domain.center.repository.CenterRepository;
import com.example.springserver.domain.center.repository.CenterSearchRepository;
import com.example.springserver.global.apiPayload.format.CenterException;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CenterService {

    private final CenterSearchRepository centerSearchRepository;
    private final CenterRepository centerRepository;

    // Elastic Search - wildcard query
    public Page<CenterDocument> searchCenters(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return centerSearchRepository.findByCenterNameContaining(keyword, pageRequest);
    }

    public Center getCenter(String centerName) {
        return centerRepository.findByCenterName(centerName)
                .orElseThrow(() -> new CenterException(ErrorCode.CENTER_NOT_FOUND));
    }
}
