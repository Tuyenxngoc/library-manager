package com.example.librarymanager.service;

import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.request.ImportReceiptRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.dto.response.GetImportReceiptResponseDto;

public interface ImportReceiptService {
    CommonResponseDto save(ImportReceiptRequestDto requestDto, String userId);

    CommonResponseDto update(Long id, ImportReceiptRequestDto requestDto, String userId);

    PaginationResponseDto<GetImportReceiptResponseDto> findAll(PaginationFullRequestDto requestDto);

    GetImportReceiptResponseDto findById(Long id);
}