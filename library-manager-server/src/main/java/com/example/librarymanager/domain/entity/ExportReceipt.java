package com.example.librarymanager.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "export_receipts")
public class ExportReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Số phiếu xuất (ID)

    @Column(name = "receipt_number", nullable = false)
    private String receiptNumber;  // Số phiếu xuất

    @Column(name = "export_date", nullable = false)
    private LocalDate exportDate;  // Ngày xuất (Ngày lập phiếu)

    @Column(name = "export_reason")
    private String exportReason;  // Lý do xuất (không bắt buộc)

}
