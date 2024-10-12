package com.example.librarymanager.service;

import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.request.LibraryVisitRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.dto.response.GetLibraryVisitResponseDto;

public interface LibraryVisitService {
    CommonResponseDto save(LibraryVisitRequestDto requestDto);

    CommonResponseDto update(Long id, LibraryVisitRequestDto requestDto);

    PaginationResponseDto<GetLibraryVisitResponseDto> findAll(PaginationFullRequestDto requestDto);

    GetLibraryVisitResponseDto findById(Long id);

    CommonResponseDto closeLibrary();
}