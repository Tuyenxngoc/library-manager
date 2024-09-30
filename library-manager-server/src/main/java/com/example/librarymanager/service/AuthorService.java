package com.example.librarymanager.service;

import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.request.AuthorRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.entity.Author;

public interface AuthorService {

    void initAuthorsFromCsv(String username);

    CommonResponseDto save(AuthorRequestDto requestDto);

    CommonResponseDto update(Long id, AuthorRequestDto requestDto);

    CommonResponseDto delete(Long id);

    PaginationResponseDto<Author> findAll(PaginationFullRequestDto requestDto);

    Author findById(Long id);

    CommonResponseDto toggleActiveStatus(Long id);
}