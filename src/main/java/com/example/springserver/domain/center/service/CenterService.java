package com.example.springserver.domain.center.service;

import com.example.springserver.domain.center.entity.Center;
import com.example.springserver.domain.center.repository.CenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CenterService {

    private final CenterRepository centerRepository;

    public List<Center> searchCenterName(String keyword) {
        return centerRepository.findByCenterNameContaining(keyword);
    }
}
