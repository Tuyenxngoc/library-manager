package com.example.librarymanager.controller;

import com.example.librarymanager.annotation.CurrentUser;
import com.example.librarymanager.annotation.RestApiV1;
import com.example.librarymanager.base.VsResponseUtil;
import com.example.librarymanager.constant.UrlConstant;
import com.example.librarymanager.domain.dto.request.HolidayRequestDto;
import com.example.librarymanager.domain.dto.request.LibraryConfigRequestDto;
import com.example.librarymanager.domain.dto.request.LibraryRulesRequestDto;
import com.example.librarymanager.security.CustomUserDetails;
import com.example.librarymanager.service.SystemSettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "System settings")
public class SystemSettingController {

    SystemSettingService systemSettingService;

    @Operation(summary = "API Update Library Rules")
    @PreAuthorize("hasRole('ROLE_MANAGE_SYSTEM_SETTINGS')")
    @PutMapping(UrlConstant.SystemSetting.UPDATE_LIBRARY_RULES)
    public ResponseEntity<?> updateLibraryRules(
            @Valid @RequestBody LibraryRulesRequestDto requestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(systemSettingService.updateLibraryRules(requestDto, userDetails.getUserId()));
    }

    @Operation(summary = "API Get Library Rules")
    @GetMapping(UrlConstant.SystemSetting.GET_LIBRARY_RULES)
    public ResponseEntity<?> getLibraryRules() {
        return VsResponseUtil.success(systemSettingService.getLibraryRules());
    }

    @Operation(summary = "API Get All Holidays")
    @GetMapping(UrlConstant.SystemSetting.GET_ALL_HOLIDAYS)
    public ResponseEntity<?> getAllHolidays(@RequestParam(required = false) Boolean activeFlag) {
        return VsResponseUtil.success(systemSettingService.getAllHolidays(activeFlag));
    }

    @Operation(summary = "API Get Holiday by ID")
    @GetMapping(UrlConstant.SystemSetting.GET_HOLIDAY_BY_ID)
    public ResponseEntity<?> getHolidayById(@PathVariable String id) {
        return VsResponseUtil.success(systemSettingService.getHolidayById(id));
    }

    @Operation(summary = "API Add a New Holiday")
    @PreAuthorize("hasRole('ROLE_MANAGE_SYSTEM_SETTINGS')")
    @PostMapping(UrlConstant.SystemSetting.ADD_HOLIDAY)
    public ResponseEntity<?> addHoliday(
            @Valid @RequestBody HolidayRequestDto holidayRequestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(HttpStatus.CREATED, systemSettingService.addHoliday(holidayRequestDto, userDetails.getUserId()));
    }

    @Operation(summary = "API Update Holiday")
    @PreAuthorize("hasRole('ROLE_MANAGE_SYSTEM_SETTINGS')")
    @PutMapping(UrlConstant.SystemSetting.UPDATE_HOLIDAY)
    public ResponseEntity<?> updateHoliday(
            @PathVariable String id,
            @Valid @RequestBody HolidayRequestDto holidayRequestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(systemSettingService.updateHoliday(id, holidayRequestDto, userDetails.getUserId()));
    }

    @Operation(summary = "API Delete Holiday")
    @PreAuthorize("hasRole('ROLE_MANAGE_SYSTEM_SETTINGS')")
    @DeleteMapping(UrlConstant.SystemSetting.DELETE_HOLIDAY)
    public ResponseEntity<?> deleteHoliday(@PathVariable String id) {
        return VsResponseUtil.success(systemSettingService.deleteHoliday(id));
    }

    @Operation(summary = "Get Library Configuration")
    @PreAuthorize("hasRole('ROLE_MANAGE_SYSTEM_SETTINGS')")
    @GetMapping(UrlConstant.SystemSetting.GET_LIBRARY_CONFIG)
    public ResponseEntity<?> getLibraryConfig() {
        return VsResponseUtil.success(systemSettingService.getLibraryConfig());
    }

    @Operation(summary = "Update Library Configuration")
    @PreAuthorize("hasRole('ROLE_MANAGE_SYSTEM_SETTINGS')")
    @PutMapping(UrlConstant.SystemSetting.UPDATE_LIBRARY_CONFIG)
    public ResponseEntity<?> updateLibraryConfig(
            @Valid @RequestBody LibraryConfigRequestDto requestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(
                systemSettingService.updateLibraryConfig(requestDto, userDetails.getUserId())
        );
    }
}
