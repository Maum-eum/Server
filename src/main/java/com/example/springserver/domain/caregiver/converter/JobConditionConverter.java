package com.example.springserver.domain.caregiver.converter;

import com.example.springserver.domain.caregiver.dto.response.JobConditionResponseDto.CertificateResponseDTO;
import com.example.springserver.domain.caregiver.dto.response.JobConditionResponseDto.ExperienceResponseDTO;
import com.example.springserver.domain.caregiver.entity.Certificate;
import com.example.springserver.domain.caregiver.entity.Experience;

public class JobConditionConverter {

    public static CertificateResponseDTO toResponseCertificate(Certificate certificate) {
        return CertificateResponseDTO.builder()
                .certNum(certificate.getCertNum())
                .certRate(certificate.getCertRate())
                .certType(certificate.getCertType())
                .build();
    }

    public static ExperienceResponseDTO toResponseExperience(Experience experience) {
        return ExperienceResponseDTO.builder()
                .duration(experience.getDuration())
                .title(experience.getTitle())
                .description(experience.getDescription())
                .build();
    }
}

