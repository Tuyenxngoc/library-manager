package com.example.librarymanager.service;

import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.request.ExportReceiptRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.dto.response.GetExportReceiptResponseDto;

public interface ExportReceiptService {
    CommonResponseDto save(ExportReceiptRequestDto requestDto, String userId);

    CommonResponseDto update(Long id, ExportReceiptRequestDto requestDto, String userId);

    CommonResponseDto delete(Long id, String userId);

    PaginationResponseDto<GetExportReceiptResponseDto> findAll(PaginationFullRequestDto requestDto);

    GetExportReceiptResponseDto findById(Long id);
}
