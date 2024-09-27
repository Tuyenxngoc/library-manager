package com.example.librarymanager.service;

import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.request.CategoryGroupRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.entity.CategoryGroup;
import jakarta.validation.Valid;

public interface CategoryGroupService {

    CommonResponseDto save(CategoryGroupRequestDto requestDto);

    CommonResponseDto update(Long id, @Valid CategoryGroupRequestDto requestDto);

    CommonResponseDto delete(Long id);

    PaginationResponseDto<CategoryGroup> findAll(PaginationFullRequestDto requestDto);

    CategoryGroup findById(Long id);

    CommonResponseDto toggleActiveStatus(Long id);

}