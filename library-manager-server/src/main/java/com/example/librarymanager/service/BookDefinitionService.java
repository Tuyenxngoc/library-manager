package com.example.librarymanager.service;

import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.request.BookDefinitionRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.dto.response.GetBookByBookDefinitionResponseDto;
import com.example.librarymanager.domain.dto.response.GetBookDefinitionResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface BookDefinitionService {
    void initBookDefinitionsFromCsv(String bookDefinitionsCsvPath);

    CommonResponseDto save(BookDefinitionRequestDto requestDto, MultipartFile image, String userId);

    CommonResponseDto update(Long id, BookDefinitionRequestDto requestDto, MultipartFile image, String userId);

    CommonResponseDto delete(Long id, String userId);

    PaginationResponseDto<GetBookDefinitionResponseDto> findAll(PaginationFullRequestDto requestDto);

    GetBookDefinitionResponseDto findById(Long id);

    CommonResponseDto toggleActiveStatus(Long id, String userId);

    PaginationResponseDto<GetBookByBookDefinitionResponseDto> getBooks(PaginationFullRequestDto requestDto, Long categoryGroupId, Long categoryId);
}