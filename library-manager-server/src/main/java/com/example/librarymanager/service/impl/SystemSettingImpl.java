package com.example.librarymanager.service.impl;

import com.example.librarymanager.constant.EventConstants;
import com.example.librarymanager.constant.SuccessMessage;
import com.example.librarymanager.domain.dto.common.CommonResponseDto;
import com.example.librarymanager.domain.dto.request.HolidayRequestDto;
import com.example.librarymanager.domain.dto.request.LibraryConfigRequestDto;
import com.example.librarymanager.domain.dto.request.LibraryRulesRequestDto;
import com.example.librarymanager.domain.dto.response.HolidayResponseDto;
import com.example.librarymanager.domain.dto.response.LibraryConfigResponseDto;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemSettingImpl implements SystemSettingService {
    private static final String LIBRARY_RULES_FILE_PATH = "data/library_rules.txt";
    private static final String HOLIDAYS_FILE_PATH = "data/holidays.txt";
    private static final String LIBRARY_CONFIG_FILE_PATH = "data/library_config.txt";

    private static final String TAG = "Thiết lập hệ thống";

    private final LogService logService;

    private final MessageSource messageSource;

    public List<HolidayResponseDto> readFromFile(String filePath) {
        List<HolidayResponseDto> holidays = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String id = parts[0];
                    String name = parts[1];
                    LocalDate startDate = LocalDate.parse(parts[2]);
                    LocalDate endDate = LocalDate.parse(parts[3]);
                    Boolean activeFlag = Boolean.valueOf(parts[4]);

                    holidays.add(new HolidayResponseDto(id, name, startDate, endDate, activeFlag));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return holidays;
    }

    public void writeToFile(String filePath, List<HolidayResponseDto> holidays) {
        List<String> lines = new ArrayList<>();
        for (HolidayResponseDto holiday : holidays) {
            String holidayData = holiday.getId() + ","
                    + holiday.getName() + ","
                    + holiday.getStartDate() + ","
                    + holiday.getEndDate() + ","
                    + holiday.getActiveFlag();
            lines.add(holidayData);
        }

        try {
            Files.write(Paths.get(filePath), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        } catch (IOException ignored) {
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

    @Override
    public List<HolidayResponseDto> getAllHolidays(Boolean activeFlag) {
        return readFromFile(HOLIDAYS_FILE_PATH).stream()
                .filter(dto -> activeFlag == null || dto.getActiveFlag().equals(activeFlag))
                .toList();
    }

    @Override
    public HolidayResponseDto getHolidayById(String id) {
        List<HolidayResponseDto> holidays = readFromFile(HOLIDAYS_FILE_PATH);
        return holidays.stream().filter(dto -> dto.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public CommonResponseDto addHoliday(HolidayRequestDto holidayRequestDto, String userId) {
        List<HolidayResponseDto> holidays = readFromFile(HOLIDAYS_FILE_PATH);

        HolidayResponseDto dto = new HolidayResponseDto();
        dto.setId(String.valueOf(holidays.size()));
        dto.setName(holidayRequestDto.getName());
        dto.setStartDate(holidayRequestDto.getStartDate());
        dto.setEndDate(holidayRequestDto.getEndDate());
        dto.setActiveFlag(holidayRequestDto.getActiveFlag());
        holidays.add(dto);

        writeToFile(HOLIDAYS_FILE_PATH, holidays);
        logService.createLog(TAG, EventConstants.ADD,
                "Thêm ngày nghỉ lễ mới: " + holidayRequestDto.getName() + ", Ngày: " + holidayRequestDto.getStartDate(), userId);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, dto);
    }

    @Override
    public CommonResponseDto updateHoliday(String id, HolidayRequestDto holidayRequestDto, String userId) {
        List<HolidayResponseDto> holidays = readFromFile(HOLIDAYS_FILE_PATH);
        HolidayResponseDto dto = null;
        for (HolidayResponseDto holidayResponseDto : holidays) {
            if (holidayResponseDto.getId().equals(id)) {
                dto = holidayResponseDto;
                break;
            }
        }
        if (dto == null) {
            return new CommonResponseDto("Holiday with ID " + id + " not found.");
        }

        dto.setName(holidayRequestDto.getName());
        dto.setStartDate(holidayRequestDto.getStartDate());
        dto.setEndDate(holidayRequestDto.getEndDate());
        dto.setActiveFlag(holidayRequestDto.getActiveFlag());
        writeToFile(HOLIDAYS_FILE_PATH, holidays);

        logService.createLog(TAG, EventConstants.EDIT,
                "Cập nhật kỳ nghỉ: " + holidayRequestDto.getName() + " (" + holidayRequestDto.getStartDate() + " - " + holidayRequestDto.getEndDate() + ")", userId);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, dto);
    }

    @Override
    public CommonResponseDto deleteHoliday(String id) {
        List<HolidayResponseDto> holidays = readFromFile(HOLIDAYS_FILE_PATH);
        HolidayResponseDto dto = null;
        for (HolidayResponseDto holidayResponseDto : holidays) {
            if (holidayResponseDto.getId().equals(id)) {
                dto = holidayResponseDto;
                break;
            }
        }
        if (dto == null) {
            return new CommonResponseDto("Holiday with ID " + id + " not found.");
        }

        holidays.remove(dto);
        writeToFile(HOLIDAYS_FILE_PATH, holidays);

        logService.createLog(TAG, EventConstants.DELETE, "Xóa kỳ nghỉ với ID: " + id, null);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public LibraryConfigResponseDto getLibraryConfig() {
        File file = new File(LIBRARY_CONFIG_FILE_PATH);

        if (!file.exists()) {
            return new LibraryConfigResponseDto(10, 30, 5, 3, 7);
        }

        try {
            List<String> lines = Files.readAllLines(file.toPath());
            int rowsPerPage = 10, reservationTime = 30, maxBorrowLimit = 5, maxRenewalTimes = 3, maxRenewalDays = 7;

            for (String line : lines) {
                if (line.startsWith("rowsPerPage=")) {
                    rowsPerPage = Integer.parseInt(line.split("=")[1]);
                } else if (line.startsWith("reservationTime=")) {
                    reservationTime = Integer.parseInt(line.split("=")[1]);
                } else if (line.startsWith("maxBorrowLimit=")) {
                    maxBorrowLimit = Integer.parseInt(line.split("=")[1]);
                } else if (line.startsWith("maxRenewalTimes=")) {
                    maxRenewalTimes = Integer.parseInt(line.split("=")[1]);
                } else if (line.startsWith("maxRenewalDays=")) {
                    maxRenewalDays = Integer.parseInt(line.split("=")[1]);
                }
            }

            return new LibraryConfigResponseDto(rowsPerPage, reservationTime, maxBorrowLimit, maxRenewalTimes, maxRenewalDays);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading library config file.");
        }
    }

    @Override
    public CommonResponseDto updateLibraryConfig(LibraryConfigRequestDto requestDto, String userId) {
        File file = new File(LIBRARY_CONFIG_FILE_PATH);

        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("rowsPerPage=" + requestDto.getRowsPerPage() + "\n");
                writer.write("reservationTime=" + requestDto.getReservationTime() + "\n");
                writer.write("maxBorrowLimit=" + requestDto.getMaxBorrowLimit() + "\n");
                writer.write("maxRenewalTimes=" + requestDto.getMaxRenewalTimes() + "\n");
                writer.write("maxRenewalDays=" + requestDto.getMaxRenewalDays() + "\n");
            }

            logService.createLog(TAG, EventConstants.EDIT, "Cập nhật cấu hình thư viện: " + LocalDateTime.now(), userId);

            String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
            return new CommonResponseDto(message);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating library config file.");
        }
    }
}
