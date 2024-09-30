package com.example.librarymanager.service;

import com.example.librarymanager.config.properties.AdminInfo;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.request.UserRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.dto.response.auth.GetCurrentUserLoginResponseDto;
import com.example.librarymanager.domain.entity.User;
import com.example.librarymanager.security.CustomUserDetails;

public interface UserService {

    void initAdmin(AdminInfo adminInfo);

    GetCurrentUserLoginResponseDto getCurrentUser(CustomUserDetails userDetails);

    CommonResponseDto save(UserRequestDto requestDto, String userId);

    CommonResponseDto update(String id, UserRequestDto requestDto, String userId);

    CommonResponseDto delete(String id, String userId);

    PaginationResponseDto<User> findAll(PaginationFullRequestDto requestDto);

    User findById(String id);
}
