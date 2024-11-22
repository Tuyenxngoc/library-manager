package com.example.librarymanager.service;

import com.example.librarymanager.domain.dto.common.CommonResponseDto;
import com.example.librarymanager.domain.dto.request.LibraryRulesRequestDto;

public interface SystemSettingService {
    CommonResponseDto updateLibraryRules(LibraryRulesRequestDto requestDto, String userId);

    String getLibraryRules();
}
