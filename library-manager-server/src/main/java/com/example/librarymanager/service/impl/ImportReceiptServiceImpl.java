package com.example.librarymanager.service.impl;

import com.example.librarymanager.domain.entity.ImportReceipt;
import com.example.librarymanager.repository.ImportReceiptRepository;
import com.example.librarymanager.service.ImportReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImportReceiptServiceImpl implements ImportReceiptService {

    @Autowired
    private ImportReceiptRepository importReceiptRepository;

    @Override
    public ImportReceipt findById(Long id) {
        return importReceiptRepository.findById(id).orElse(null);
    }

    @Override
    public List<ImportReceipt> findAll() {
        return importReceiptRepository.findAll();
    }

    @Override
    public ImportReceipt save(ImportReceipt importReceipt) {
        return importReceiptRepository.save(importReceipt);
    }

    @Override
    public void delete(Long id) {
        importReceiptRepository.deleteById(id);
    }
}