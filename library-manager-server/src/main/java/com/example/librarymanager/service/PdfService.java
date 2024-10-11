package com.example.librarymanager.service;

import com.example.librarymanager.domain.dto.request.CreateReaderCardsRequestDto;
import com.example.librarymanager.domain.entity.Reader;

import java.util.List;

public interface PdfService {
    byte[] createPdf(CreateReaderCardsRequestDto requestDto, List<Reader> readers);
}
