package com.example.librarymanager.service;

import com.example.librarymanager.domain.dto.response.statistics.BorrowStatisticsResponseDto;
import com.example.librarymanager.domain.dto.response.statistics.LibraryStatisticsResponseDto;

public interface StatisticsService {
    LibraryStatisticsResponseDto getLibraryStatistics();

    BorrowStatisticsResponseDto getBorrowStatistics();
}
