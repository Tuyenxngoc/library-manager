package com.example.librarymanager.service;

import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.request.BookSetRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.dto.response.GetBookSetResponseDto;
import com.example.librarymanager.domain.entity.BookSet;

public interface BookSetService {
    CommonResponseDto save(BookSetRequestDto requestDto);

    CommonResponseDto update(Long id, BookSetRequestDto requestDto);

    CommonResponseDto delete(Long id);

    PaginationResponseDto<GetBookSetResponseDto> findAll(PaginationFullRequestDto requestDto);

    BookSet findById(Long id);

    CommonResponseDto toggleActiveStatus(Long id);
}
