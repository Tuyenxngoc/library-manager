package com.example.librarymanager.service;

import com.example.librarymanager.domain.dto.filter.Filter;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.pagination.PaginationSortRequestDto;
import com.example.librarymanager.domain.dto.request.BookDefinitionRequestDto;
import com.example.librarymanager.domain.dto.response.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookDefinitionService {
    void initBookDefinitionsFromCsv(String bookDefinitionsCsvPath);

    CommonResponseDto save(BookDefinitionRequestDto requestDto, MultipartFile image, String userId);

    CommonResponseDto update(Long id, BookDefinitionRequestDto requestDto, MultipartFile image, String userId);

    CommonResponseDto delete(Long id, String userId);

    PaginationResponseDto<GetBookDefinitionResponseDto> findAll(PaginationFullRequestDto requestDto);

    GetBookDefinitionResponseDto findById(Long id);

    CommonResponseDto toggleActiveStatus(Long id, String userId);

    PaginationResponseDto<GetBookByBookDefinitionResponseDto> getBooks(PaginationFullRequestDto requestDto, Long categoryGroupId, Long categoryId);

    PaginationResponseDto<GetBookForUserResponseDto> getBooksForUser(PaginationFullRequestDto requestDto, Long categoryGroupId, Long categoryId);

    GetBookDetailForUserResponseDto getBookDetailForUser(Long id);

    PaginationResponseDto<GetBookForUserResponseDto> advancedSearchBooks(List<Filter> filters, PaginationSortRequestDto requestDto);
}