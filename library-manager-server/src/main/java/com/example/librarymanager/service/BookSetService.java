package com.example.librarymanager.service;

import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.request.BookSetRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.dto.response.GetBookSetResponseDto;
import com.example.librarymanager.domain.entity.BookSet;
import jakarta.validation.Valid;

public interface BookSetService {
    CommonResponseDto save(@Valid BookSetRequestDto requestDto);

    CommonResponseDto update(Long id, @Valid BookSetRequestDto requestDto);

    CommonResponseDto delete(Long id);

    PaginationResponseDto<GetBookSetResponseDto> findAll(PaginationFullRequestDto requestDto);

    BookSet findById(Long id);

    CommonResponseDto toggleActiveStatus(Long id);
}
