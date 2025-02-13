package com.example.springserver.service;

import com.example.springserver.domain.caregiver.entity.Caregiver;
import com.example.springserver.domain.caregiver.repository.CaregiverRepository;
import com.example.springserver.domain.center.entity.Admin;
import com.example.springserver.domain.center.repository.AdminRepository;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import com.example.springserver.global.security.util.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final CaregiverRepository caregiverRepository;

    public CustomUserDetailsService(AdminRepository adminRepository, CaregiverRepository caregiverRepository) {
        this.adminRepository = adminRepository;
        this.caregiverRepository = caregiverRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Admin> adminOpt = adminRepository.findByUsername(username);
        if (adminOpt.isPresent()) {
            return new CustomUserDetails(adminOpt.get());
        }

        Optional<Caregiver> workerOpt = caregiverRepository.findByUsername(username);
        if (workerOpt.isPresent()) {
            return new CustomUserDetails(workerOpt.get());
        }

        throw new GlobalException(ErrorCode.MEMBER_NOT_FOUND);
    }
}
