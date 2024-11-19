package com.example.librarymanager.service;

import com.example.librarymanager.domain.dto.common.CommonResponseDto;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.response.bookborrow.BookBorrowResponseDto;

import java.util.Set;

public interface BookBorrowService {

    PaginationResponseDto<BookBorrowResponseDto> findAll(PaginationFullRequestDto requestDto);

    CommonResponseDto returnBooksByIds(Set<Long> ids, String userId);

}