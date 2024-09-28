package com.example.librarymanager.service;

import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.request.PublisherRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.entity.Publisher;
import jakarta.validation.Valid;

public interface PublisherService {

    CommonResponseDto save(@Valid PublisherRequestDto requestDto);

    CommonResponseDto update(Long id, @Valid PublisherRequestDto requestDto);

    CommonResponseDto delete(Long id);

    PaginationResponseDto<Publisher> findAll(PaginationFullRequestDto requestDto);

    Publisher findById(Long id);

    CommonResponseDto toggleActiveStatus(Long id);

}