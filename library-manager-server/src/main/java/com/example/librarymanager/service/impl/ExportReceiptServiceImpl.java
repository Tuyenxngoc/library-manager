package com.example.librarymanager.service.impl;

import com.example.librarymanager.domain.entity.ExportReceipt;
import com.example.librarymanager.repository.ExportReceiptRepository;
import com.example.librarymanager.service.ExportReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExportReceiptServiceImpl implements ExportReceiptService {

    @Autowired
    private ExportReceiptRepository exportReceiptRepository;

    @Override
    public ExportReceipt findById(Long id) {
        return exportReceiptRepository.findById(id).orElse(null);
    }

    @Override
    public List<ExportReceipt> findAll() {
        return exportReceiptRepository.findAll();
    }

    @Override
    public ExportReceipt save(ExportReceipt exportReceipt) {
        return exportReceiptRepository.save(exportReceipt);
    }

    @Override
    public void delete(Long id) {
        exportReceiptRepository.deleteById(id);
    }
}