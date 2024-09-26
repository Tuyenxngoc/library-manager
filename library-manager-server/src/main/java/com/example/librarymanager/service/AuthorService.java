package com.example.librarymanager.service;

import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.entity.Author;
import jakarta.validation.Valid;

public interface AuthorService {
    Author findById(Long id);

    PaginationResponseDto<Author> findAll(PaginationFullRequestDto requestDto);

    Author save(Author author);

    Author update(Long id, @Valid Author requestDto);

    CommonResponseDto delete(Long id);
}