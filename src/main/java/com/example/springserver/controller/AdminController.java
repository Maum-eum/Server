package com.example.springserver.controller;

import com.example.springserver.apiPayload.ApiResponse;
import com.example.springserver.converter.AdminConverter;
import com.example.springserver.converter.CaregiverConverter;
import com.example.springserver.domain.entity.Admin;
import com.example.springserver.domain.entity.Caregiver;
import com.example.springserver.dto.AdminDTO.AdminRequestDTO;
import com.example.springserver.dto.AdminDTO.AdminResponseDTO;
import com.example.springserver.dto.CaregiverDTO.CaregiverRequestDTO;
import com.example.springserver.dto.CaregiverDTO.CaregiverResponseDTO;
import com.example.springserver.service.JoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@Tag(name = "Admin API", description = "Admin에 해당하는 센터장, 사회복지사에 대한 API")
@RequestMapping("/admin")
public class AdminController {


    private final JoinService joinService;

    public AdminController (JoinService joinService) {
        this.joinService = joinService;
    }

    @Operation(summary = "회원가입", description = "Post")
    @PostMapping("/signup")
    public ApiResponse<AdminResponseDTO.SignUpAdminResult> signUpAdmin(@RequestBody @Valid AdminRequestDTO.SignUpAdminReq request){

        Admin newAdmin = joinService.signUpAdmin(request);

        return ApiResponse.onSuccess(AdminConverter.toSignUpAdminResult(newAdmin));
    }
}
