package com.example.librarymanager.service;

import com.example.librarymanager.config.properties.AdminInfo;
import com.example.librarymanager.domain.dto.response.auth.GetCurrentUserLoginResponseDto;
import com.example.librarymanager.domain.entity.User;
import com.example.librarymanager.security.CustomUserDetails;

public interface UserService {

    void initAdmin(AdminInfo adminInfo);

    User getUserById(String userId);

    GetCurrentUserLoginResponseDto getCurrentUser(CustomUserDetails userDetails);

}
