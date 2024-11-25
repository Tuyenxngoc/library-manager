package com.example.librarymanager.repository;

import com.example.librarymanager.domain.entity.BorrowReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BorrowReceiptRepository extends JpaRepository<BorrowReceipt, Long>, JpaSpecificationExecutor<BorrowReceipt> {
    boolean existsByReceiptNumber(String receiptNumber);

    List<BorrowReceipt> findAllByIdIn(Set<Long> borrowIds);
}
