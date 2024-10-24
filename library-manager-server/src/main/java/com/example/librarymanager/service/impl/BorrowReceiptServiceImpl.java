package com.example.librarymanager.service.impl;

import com.example.librarymanager.repository.BorrowReceiptRepository;
import com.example.librarymanager.service.BorrowReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BorrowReceiptServiceImpl implements BorrowReceiptService {

    private BorrowReceiptRepository borrowReceiptRepository;

}