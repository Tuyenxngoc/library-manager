package com.example.librarymanager.service;

import com.example.librarymanager.domain.entity.ImportReceipt;

import java.util.List;

public interface ImportReceiptService {
    ImportReceipt findById(Long id);

    List<ImportReceipt> findAll();

    ImportReceipt save(ImportReceipt importReceipt);

    void delete(Long id);
}