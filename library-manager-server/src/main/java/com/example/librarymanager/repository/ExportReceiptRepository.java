package com.example.librarymanager.repository;

import com.example.librarymanager.domain.entity.ExportReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExportReceiptRepository extends JpaRepository<ExportReceipt, Long> {
}
