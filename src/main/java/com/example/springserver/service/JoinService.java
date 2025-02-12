package com.example.springserver.service;

import com.example.springserver.apiPayload.code.status.ErrorStatus;
import com.example.springserver.apiPayload.exception.GeneralException;
import com.example.springserver.converter.AdminConverter;
import com.example.springserver.converter.CaregiverConverter;
import com.example.springserver.domain.entity.Admin;
import com.example.springserver.domain.entity.Caregiver;
import com.example.springserver.domain.entity.Center;
import com.example.springserver.dto.AdminDTO.AdminRequestDTO;
import com.example.springserver.dto.CaregiverDTO.CaregiverRequestDTO;
import com.example.springserver.repository.AdminRepository;
import com.example.springserver.repository.CaregiverRepository;
import com.example.springserver.repository.center.CenterRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    private final CaregiverRepository caregiverRepository;
    private final CenterRepository centerRepository;
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(CaregiverRepository caregiverRepository, CenterRepository centerRepository, AdminRepository adminRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.caregiverRepository = caregiverRepository;
        this.centerRepository = centerRepository;
        this.adminRepository = adminRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Caregiver signUpCaregiver(CaregiverRequestDTO.SignUpCaregiverReq request) {

        Boolean isExist = caregiverRepository.existsByUsername(request.getUsername());

        if(isExist){
            throw new GeneralException(ErrorStatus.MEMBER_IS_EXIST);
        }

        // Caregiver 객체 converter를 통해 생성
        Caregiver newCaregiver = CaregiverConverter.toCaregiver(request, bCryptPasswordEncoder);

        return caregiverRepository.save(newCaregiver);
    }

    public Admin signUpAdmin(AdminRequestDTO.SignUpAdminReq request) {

        Boolean isExist = adminRepository.existsByUsername(request.getUsername());

        if(isExist){
            throw new GeneralException(ErrorStatus.MEMBER_IS_EXIST);
        }

        Center centerData = centerRepository.findByCenterName(request.getCenterName())
                .orElseThrow(() -> new GeneralException(ErrorStatus.CENTER_NOT_FOUND));

        // Admin 객체 converter를 통해 생성
        Admin newAdmin = AdminConverter.toAdmin(request, bCryptPasswordEncoder, centerData);

        return adminRepository.save(newAdmin);
    }

}
