package com.example.springserver.service.location;

import com.example.springserver.domain.location.entity.Sido;
import com.example.springserver.repository.location.SidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SidoService {

    private final SidoRepository sidoRepository;

    public List<Sido> getSidoList() {
        return sidoRepository.findAll();
    }
}
