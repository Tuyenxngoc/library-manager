package com.example.librarymanager.controller;

import com.example.librarymanager.annotation.RestApiV1;
import com.example.librarymanager.base.VsResponseUtil;
import com.example.librarymanager.constant.UrlConstant;
import com.example.librarymanager.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Statistics")
public class StatsController {

    private final StatisticsService statisticsService;

    @Operation(summary = "Get Library Statistics")
    @GetMapping(UrlConstant.Stats.GET_LIBRARY_STATISTICS)
    public ResponseEntity<?> getLibraryStatistics() {
        return VsResponseUtil.success(statisticsService.getLibraryStatistics());

    }
}
