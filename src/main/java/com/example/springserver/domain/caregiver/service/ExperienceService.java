package com.example.springserver.domain.caregiver.service;

import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDTO;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDTO.ExperienceRequestDTO;
import com.example.springserver.domain.caregiver.entity.Experience;
import com.example.springserver.domain.caregiver.repository.ExperienceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExperienceService {

    private final ExperienceRepository experienceRepository;

    public void registExper(List<ExperienceRequestDTO> experienceRequestDTOList) {
        if (experienceRequestDTOList==null)
            return ;

    }
}
