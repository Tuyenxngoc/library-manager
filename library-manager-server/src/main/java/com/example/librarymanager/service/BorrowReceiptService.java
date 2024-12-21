package com.example.librarymanager.service;

import com.example.librarymanager.domain.dto.common.CommonResponseDto;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.request.BorrowReceiptRequestDto;
import com.example.librarymanager.domain.dto.request.CreateBorrowReceiptRequestDto;
import com.example.librarymanager.domain.dto.response.borrowreceipt.BorrowReceiptDetailResponseDto;
import com.example.librarymanager.domain.dto.response.borrowreceipt.BorrowReceiptDetailsDto;
import com.example.librarymanager.domain.dto.response.borrowreceipt.BorrowReceiptForReaderResponseDto;
import com.example.librarymanager.domain.dto.response.borrowreceipt.BorrowReceiptResponseDto;

public interface BorrowReceiptService {
    String generateReceiptNumber();

    CommonResponseDto save(BorrowReceiptRequestDto requestDto, String userId);

    CommonResponseDto update(Long id, BorrowReceiptRequestDto requestDto, String userId);

    CommonResponseDto delete(Long id, String userId);

    PaginationResponseDto<BorrowReceiptResponseDto> findAll(PaginationFullRequestDto requestDto);

    BorrowReceiptDetailResponseDto findById(Long id);

    BorrowReceiptDetailResponseDto findByCartId(Long id);

    PaginationResponseDto<BorrowReceiptForReaderResponseDto> findByCardNumber(String cardNumber, PaginationFullRequestDto requestDto);

    BorrowReceiptDetailsDto findDetailsById(Long id);

    byte[] createPdfForReceipts(CreateBorrowReceiptRequestDto requestDto, String userId);
}
