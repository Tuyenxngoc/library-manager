package com.example.librarymanager.service;

import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.request.BookDefinitionRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.entity.BookDefinition;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

public interface BookDefinitionService {

    CommonResponseDto save(@Valid BookDefinitionRequestDto requestDto, MultipartFile image);

    CommonResponseDto update(Long id, @Valid BookDefinitionRequestDto requestDto);

    CommonResponseDto delete(Long id);

    PaginationResponseDto<BookDefinition> findAll(PaginationFullRequestDto requestDto);

    BookDefinition findById(Long id);

    CommonResponseDto toggleActiveStatus(Long id);
}