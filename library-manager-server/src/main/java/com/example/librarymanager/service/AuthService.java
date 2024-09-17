package com.example.librarymanager.service;

import com.example.librarymanager.domain.dto.request.auth.*;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.dto.response.auth.LoginResponseDto;
import com.example.librarymanager.domain.dto.response.auth.TokenRefreshResponseDto;
import com.example.librarymanager.domain.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public interface AuthService {

    LoginResponseDto login(LoginRequestDto request);

    CommonResponseDto logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication);

    TokenRefreshResponseDto refresh(TokenRefreshRequestDto request);

    User register(RegisterRequestDto requestDto, String siteURL);

    CommonResponseDto forgetPassword(ForgetPasswordRequestDto requestDto);

    CommonResponseDto changePassword(ChangePasswordRequestDto requestDto, String username);
}
