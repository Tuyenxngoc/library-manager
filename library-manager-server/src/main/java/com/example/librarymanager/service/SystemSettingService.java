package com.example.librarymanager.service;

import com.example.librarymanager.domain.dto.common.CommonResponseDto;
import com.example.librarymanager.domain.dto.request.LibraryConfigRequestDto;
import com.example.librarymanager.domain.dto.request.LibraryRulesRequestDto;
import com.example.librarymanager.domain.dto.response.LibraryConfigResponseDto;

public interface SystemSettingService {
    CommonResponseDto updateLibraryRules(LibraryRulesRequestDto requestDto, String userId);

    String getLibraryRules();

    LibraryConfigResponseDto getLibraryConfig();

    CommonResponseDto updateLibraryConfig(LibraryConfigRequestDto requestDto, String userId);
}
