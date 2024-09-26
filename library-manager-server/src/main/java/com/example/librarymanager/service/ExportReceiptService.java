package com.example.librarymanager.service;

import com.example.librarymanager.domain.entity.ExportReceipt;

import java.util.List;

public interface ExportReceiptService {
    ExportReceipt findById(Long id);

    List<ExportReceipt> findAll();

    ExportReceipt save(ExportReceipt exportReceipt);

    void delete(Long id);
}
