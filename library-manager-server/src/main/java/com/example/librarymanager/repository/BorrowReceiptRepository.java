package com.example.librarymanager.repository;

import com.example.librarymanager.domain.entity.BorrowReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowReceiptRepository extends JpaRepository<BorrowReceipt, Long> {
}
