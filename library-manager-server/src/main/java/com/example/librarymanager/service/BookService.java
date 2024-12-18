package com.example.librarymanager.service;

import com.example.librarymanager.domain.dto.common.CommonResponseDto;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.request.BookRequestDto;
import com.example.librarymanager.domain.dto.response.book.BookResponseDto;

import java.util.List;
import java.util.Set;

public interface BookService {
    CommonResponseDto update(Long id, BookRequestDto requestDto, String userId);

    PaginationResponseDto<BookResponseDto> findAll(PaginationFullRequestDto requestDto);

    List<BookResponseDto> findByIds(Set<Long> ids);

    BookResponseDto findById(Long id);

    byte[] getBooksPdfContent(List<Long> bookIds);

    byte[] getBooksLabelType1PdfContent(List<Long> bookIds);

    byte[] getBooksLabelType2PdfContent(List<Long> bookIds);

    byte[] generateBookListPdf();
}