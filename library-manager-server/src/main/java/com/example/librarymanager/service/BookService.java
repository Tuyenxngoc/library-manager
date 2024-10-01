package com.example.librarymanager.service;

import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.request.BookRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.dto.response.GetBookResponseDto;

public interface BookService {
    CommonResponseDto update(Long id, BookRequestDto requestDto, String userId);

    PaginationResponseDto<GetBookResponseDto> findAll(PaginationFullRequestDto requestDto);

    GetBookResponseDto findById(Long id);
}