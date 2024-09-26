package com.example.librarymanager.service;

import com.example.librarymanager.domain.entity.BorrowReceipt;

import java.util.List;

public interface BorrowReceiptService {
    BorrowReceipt findById(Long id);

    List<BorrowReceipt> findAll();

    BorrowReceipt save(BorrowReceipt borrowReceipt);

    void delete(Long id);
}
