package com.example.librarymanager.service.impl;

import com.example.librarymanager.constant.EventConstants;
import com.example.librarymanager.constant.SuccessMessage;
import com.example.librarymanager.domain.dto.common.CommonResponseDto;
import com.example.librarymanager.domain.dto.request.LibraryRulesRequestDto;
import com.example.librarymanager.service.LogService;
import com.example.librarymanager.service.SystemSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SystemSettingImpl implements SystemSettingService {
    private static final String LIBRARY_RULES_FILE_PATH = "data/library_rules.txt";

    private static final String TAG = "Thiết lập hệ thống";

    private final LogService logService;

    private final MessageSource messageSource;

    @Override
    public CommonResponseDto updateLibraryRules(LibraryRulesRequestDto requestDto, String userId) {
        try {
            File file = new File(LIBRARY_RULES_FILE_PATH);

            if (!file.exists()) {
                file.createNewFile();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(requestDto.getContent());
            }
        } catch (IOException e) {

        }

        logService.createLog(TAG, EventConstants.EDIT, "Cập nhật nội quy thư viện ngày: " + LocalDateTime.now(), userId);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public String getLibraryRules() {
        File file = new File(LIBRARY_RULES_FILE_PATH);

        if (!file.exists()) {
            return "Library rules file not found.";
        }

        try {
            return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(LIBRARY_RULES_FILE_PATH)));
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading library rules: " + e.getMessage();
        }
    }
}
