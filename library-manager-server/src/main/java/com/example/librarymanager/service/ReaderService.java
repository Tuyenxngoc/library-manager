package com.example.librarymanager.service;

import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.request.CreateReaderCardsRequestDto;
import com.example.librarymanager.domain.dto.request.ReaderRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.dto.response.GetReaderDetailResponseDto;
import com.example.librarymanager.domain.dto.response.GetReaderResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface ReaderService {
    void initReadersFromCsv(String readersCsvPath);

    CommonResponseDto save(ReaderRequestDto requestDto, MultipartFile image, String userId);

    CommonResponseDto update(Long id, ReaderRequestDto requestDto, MultipartFile image, String userId);

    CommonResponseDto delete(Long id, String userId);

    PaginationResponseDto<GetReaderResponseDto> findAll(PaginationFullRequestDto requestDto);

    GetReaderResponseDto findById(Long id);

    GetReaderResponseDto findByCardNumber(String cardNumber);

    byte[] generateReaderCards(CreateReaderCardsRequestDto requestDto);

    GetReaderDetailResponseDto getReaderDetailsByCardNumber(String cardNumber);
}
