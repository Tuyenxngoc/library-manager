package com.example.librarymanager.service;

import com.example.librarymanager.domain.dto.common.CommonResponseDto;
import com.example.librarymanager.domain.dto.request.HolidayRequestDto;
import com.example.librarymanager.domain.dto.request.LibraryRulesRequestDto;
import com.example.librarymanager.domain.dto.response.HolidayResponseDto;

import java.util.List;

public interface SystemSettingService {
    CommonResponseDto updateLibraryRules(LibraryRulesRequestDto requestDto, String userId);

    String getLibraryRules();

    List<HolidayResponseDto> getAllHolidays(Boolean activeFlag);

    HolidayResponseDto getHolidayById(String id);

    CommonResponseDto addHoliday(HolidayRequestDto holidayRequestDto, String userId);

    CommonResponseDto updateHoliday(String id, HolidayRequestDto holidayRequestDto, String userId);

    CommonResponseDto deleteHoliday(String id);
}
