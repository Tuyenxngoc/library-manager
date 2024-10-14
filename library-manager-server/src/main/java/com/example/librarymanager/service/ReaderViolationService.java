package com.example.librarymanager.service;

import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.request.ReaderViolationRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.dto.response.GetReaderViolationResponseDto;

public interface ReaderViolationService {
    CommonResponseDto save(ReaderViolationRequestDto requestDto, String userId);

    CommonResponseDto update(Long id, ReaderViolationRequestDto requestDto, String userId);

    CommonResponseDto delete(Long id, String userId);

    PaginationResponseDto<GetReaderViolationResponseDto> findAll(PaginationFullRequestDto requestDto);

    GetReaderViolationResponseDto findById(Long id);
}
