package com.example.librarymanager.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "import_receipts")
public class ImportReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "import_receipt_id")
    private Long id;  // Số phiếu nhập (ID)

    @Column(name = "receipt_number", nullable = false)
    private String receiptNumber;  // Số phiếu nhập

    @Column(name = "import_date", nullable = false)
    private LocalDate importDate;  // Ngày nhập

    @Column(name = "general_record_number", nullable = false)
    private String generalRecordNumber;  // Số vào sổ tổng quát

    @Column(name = "funding_source")
    private String fundingSource;  // Nguồn cấp

    @Column(name = "import_reason")
    private String importReason;  // Lý do nhập

}
