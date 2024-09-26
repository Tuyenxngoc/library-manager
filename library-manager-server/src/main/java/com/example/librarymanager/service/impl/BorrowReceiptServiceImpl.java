package com.example.librarymanager.service.impl;

import com.example.librarymanager.domain.entity.BorrowReceipt;
import com.example.librarymanager.repository.BorrowReceiptRepository;
import com.example.librarymanager.service.BorrowReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowReceiptServiceImpl implements BorrowReceiptService {

    @Autowired
    private BorrowReceiptRepository borrowReceiptRepository;

    @Override
    public BorrowReceipt findById(Long id) {
        return borrowReceiptRepository.findById(id).orElse(null);
    }

    @Override
    public List<BorrowReceipt> findAll() {
        return borrowReceiptRepository.findAll();
    }

    @Override
    public BorrowReceipt save(BorrowReceipt borrowReceipt) {
        return borrowReceiptRepository.save(borrowReceipt);
    }

    @Override
    public void delete(Long id) {
        borrowReceiptRepository.deleteById(id);
    }
}