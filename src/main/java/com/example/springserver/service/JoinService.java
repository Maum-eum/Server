package com.example.springserver.service;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDTO.CertificateRequestDTO;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDTO.ExperienceRequestDTO;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDTO.SignUpCaregiverReq;
import com.example.springserver.domain.caregiver.repository.CertificateRepository;
import com.example.springserver.domain.caregiver.repository.ExperienceRepository;
import com.example.springserver.domain.center.converter.AdminConverter;
import com.example.springserver.domain.caregiver.converter.CaregiverConverter;
import com.example.springserver.domain.center.entity.Admin;
import com.example.springserver.domain.caregiver.entity.Caregiver;
import com.example.springserver.domain.center.entity.Center;
import com.example.springserver.domain.center.dto.request.AdminRequestDTO;
import com.example.springserver.domain.center.repository.AdminRepository;
import com.example.springserver.domain.caregiver.repository.CaregiverRepository;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import com.example.springserver.domain.center.repository.CenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JoinService {

    private final CaregiverRepository caregiverRepository;
    private final CenterRepository centerRepository;
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CertificateRepository certificateRepository;
    private final ExperienceRepository experienceRepository;

    @Transactional
    public Caregiver signUpCaregiver(SignUpCaregiverReq request) {

        List<CertificateRequestDTO> certificateRequestDTOList = request.getCertificateRequestDTOList();
        List<ExperienceRequestDTO> experienceRequestDTOList = request.getExperienceRequestDTOList();

        Boolean isAdminExist = adminRepository.existsByUsername(request.getUsername());
        Boolean isCaregiverExist = caregiverRepository.existsByUsername(request.getUsername());

        if(isAdminExist || isCaregiverExist){
            throw new GlobalException(ErrorCode.MEMBER_IS_EXIST);
        }

        //기본이미지 적용

        if(request.getImg()==null)
            request.setCommonImg();

        Caregiver saved = caregiverRepository.save(CaregiverConverter.toCaregiver(request, bCryptPasswordEncoder));

        //자격증저장
        if (certificateRequestDTOList!=null)
            for(CertificateRequestDTO dto : certificateRequestDTOList)
                certificateRepository.save(CaregiverConverter.toCertificate(saved,dto));

        //경력저장
        if (experienceRequestDTOList!=null)
            for (ExperienceRequestDTO dto : experienceRequestDTOList)
                experienceRepository.save(CaregiverConverter.toExperience(saved,dto));

        return saved;
    }

    @Transactional
    public Admin signUpAdmin(AdminRequestDTO.SignUpAdminReq request) {

        Boolean isAdminExist = adminRepository.existsByUsername(request.getUsername());
        Boolean isCaregiverExist = caregiverRepository.existsByUsername(request.getUsername());

        if(isAdminExist || isCaregiverExist){
            throw new GlobalException(ErrorCode.MEMBER_IS_EXIST);
        }

        Center centerData = centerRepository.findByCenterName(request.getCenterName())
                .orElseThrow(() -> new GlobalException(ErrorCode.CENTER_NOT_FOUND));

        // Admin 객체 converter를 통해 생성
        Admin newAdmin = AdminConverter.toAdmin(request, bCryptPasswordEncoder, centerData);

        // 양방향 연관관계 매핑
        newAdmin.changeCenter(centerData);

        return adminRepository.save(newAdmin);
    }

}
