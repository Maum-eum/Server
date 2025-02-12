package com.example.springserver.domain.center.controller;

import com.example.springserver.domain.center.converter.AdminConverter;
import com.example.springserver.domain.center.entity.Admin;
import com.example.springserver.domain.center.dto.request.AdminRequestDTO;
import com.example.springserver.domain.center.dto.response.AdminResponseDTO;
import com.example.springserver.service.JoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    public AdminResponseDTO.SignUpAdminResult signUpAdmin(@RequestBody @Valid AdminRequestDTO.SignUpAdminReq request){

        Admin newAdmin = joinService.signUpAdmin(request);

        return AdminConverter.toSignUpAdminResult(newAdmin);
    }
}
