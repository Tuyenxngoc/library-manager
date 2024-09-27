package com.example.librarymanager.service;

import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.request.CategoryRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.dto.response.GetCategoryResponseDto;
import com.example.librarymanager.domain.entity.Category;

public interface CategoryService {
    Category findById(Long id);

    PaginationResponseDto<GetCategoryResponseDto> findAll(PaginationFullRequestDto requestDto);

    CommonResponseDto save(CategoryRequestDto requestDto);

    CommonResponseDto delete(Long id);

    CommonResponseDto update(Long id, CategoryRequestDto requestDto);

    CommonResponseDto toggleActiveStatus(Long id);

}