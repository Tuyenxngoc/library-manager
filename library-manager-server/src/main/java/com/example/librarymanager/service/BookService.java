package com.example.librarymanager.service;

import com.example.librarymanager.constant.BookCondition;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.response.book.BookResponseDto;

import java.util.List;
import java.util.Set;

public interface BookService {
    PaginationResponseDto<BookResponseDto> findAll(PaginationFullRequestDto requestDto, BookCondition bookCondition);

    List<BookResponseDto> findByIds(Set<Long> ids);

    BookResponseDto findById(Long id);
}