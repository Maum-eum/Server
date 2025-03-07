package com.example.springserver.service;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDto.CertificateRequestDTO;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDto.ExperienceRequestDTO;
import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDto.SignUpCaregiverReqDto;
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
import org.springframework.web.multipart.MultipartFile;

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
    private final S3Service s3Service;


    @Transactional
    public Caregiver signUpCaregiver(SignUpCaregiverReqDto request, MultipartFile profileImg) {

        List<CertificateRequestDTO> certificateRequestDTOList = request.getCertificateRequestDTOList();
        List<ExperienceRequestDTO> experienceRequestDTOList = request.getExperienceRequestDTOList();

        Boolean isAdminExist = adminRepository.existsByUsername(request.getUsername());
        Boolean isCaregiverExist = caregiverRepository.existsByUsername(request.getUsername());

        if(isAdminExist || isCaregiverExist){
            throw new GlobalException(ErrorCode.USERNAME_IS_EXIST);
        }

        // 이미지 적용
        String imgUrl;
        if(profileImg == null) {
            imgUrl = "http://localhost:8080/basicImg.jpeg";
        } else {
            imgUrl = s3Service.uploadFileImage(profileImg);
        }

        Caregiver saved = caregiverRepository.save(CaregiverConverter.toCaregiver(request, bCryptPasswordEncoder, imgUrl));

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
        if(!centerData.getCertification().equals(request.getCenterCertification()))
            throw new GlobalException(ErrorCode.CENTER_CERTIFICATION_FAIL);

        Admin newAdmin = AdminConverter.toAdmin(request, bCryptPasswordEncoder, centerData);

        // 양방향 연관관계 매핑
        newAdmin.changeCenter(centerData);

        return adminRepository.save(newAdmin);
    }

}
