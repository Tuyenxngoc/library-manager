package com.example.librarymanager.service;

import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.request.BorrowReceiptRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.dto.response.GetBorrowReceiptDetailResponseDto;
import com.example.librarymanager.domain.dto.response.GetBorrowReceiptForReaderResponseDto;
import com.example.librarymanager.domain.dto.response.GetBorrowReceiptResponseDto;

public interface BorrowReceiptService {

    CommonResponseDto save(BorrowReceiptRequestDto requestDto, String userId);

    CommonResponseDto update(Long id, BorrowReceiptRequestDto requestDto, String userId);

    CommonResponseDto delete(Long id, String userId);

    PaginationResponseDto<GetBorrowReceiptResponseDto> findAll(PaginationFullRequestDto requestDto);

    GetBorrowReceiptDetailResponseDto findById(Long id);

    GetBorrowReceiptDetailResponseDto findByCartId(Long id);

    PaginationResponseDto<GetBorrowReceiptForReaderResponseDto> findByCardNumber(String cardNumber, PaginationFullRequestDto requestDto);
}
