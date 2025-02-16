package com.example.springserver.global.security.jwt;

import com.example.springserver.domain.caregiver.entity.Caregiver;
import com.example.springserver.domain.center.entity.Admin;
import com.example.springserver.domain.center.repository.AdminRepository;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import com.example.springserver.global.security.util.CustomUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final AdminRepository adminRepository;

    public JWTFilter(JWTUtil jwtUtil, AdminRepository adminRepository){
        this.jwtUtil = jwtUtil;
        this.adminRepository = adminRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Authorization 헤더에서 access token을 꺼냄
        String accessToken = request.getHeader("Authorization");

        // 토큰이 없다면 다음 필터로 넘김
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // "Bearer "를 제거하고 실제 토큰만 남김
        accessToken = accessToken.substring(7);

        // 토큰 만료 여부 확인, 만료시 예외 발생
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            throw new GlobalException(ErrorCode.ACCESS_TOKEN_EXPIRED);
        }

        // 토큰이 access인지 확인 (발급 시 페이로드에 명시)
        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals("access")) {
            throw new GlobalException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        //토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);
        Long userId = jwtUtil.getUserId(accessToken);

        // 역할에 맞는 객체 생성
        CustomUserDetails customUserDetails;
        if ("ROLE_ADMIN".equalsIgnoreCase(role)) {
            // Admin 정보와 center 정보 조회
            Admin admin = adminRepository.findByUsername(username)
                    .orElseThrow(() -> new GlobalException(ErrorCode.ADMIN_NOT_FOUND));
            customUserDetails = new CustomUserDetails(admin);
        } else if ("ROLE_CAREGIVER".equalsIgnoreCase(role)) {
            Caregiver caregiver = Caregiver.builder()
                    .id(userId)
                    .username(username)
                    .password("")
                    .build();
            customUserDetails = new CustomUserDetails(caregiver);
        } else {
            throw new GlobalException(ErrorCode.INVALID_MEMBER_ROLE);
        }

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
