package com.example.springserver.domain.caregiver.service;

import com.example.springserver.domain.caregiver.converter.CaregiverConverter;
import com.example.springserver.domain.caregiver.repository.CertificateRepository;
import com.example.springserver.domain.caregiver.repository.ExperienceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDTO.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CareGiverService {

}
