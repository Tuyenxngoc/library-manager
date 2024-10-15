package com.example.librarymanager.domain.entity;

import com.example.librarymanager.domain.entity.common.DateAuditing;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "export_receipts",
        uniqueConstraints = @UniqueConstraint(name = "UN_EXPORT_RECEIPTS_RECEIPT_NUMBER", columnNames = "receipt_number"))
public class ExportReceipt extends DateAuditing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "export_receipt_id")
    private Long id;

    @Column(name = "receipt_number", nullable = false)
    private String receiptNumber;  // Số phiếu xuất

    @Column(name = "export_date", nullable = false)
    private LocalDate exportDate;  // Ngày xuất (Ngày lập phiếu)

    @Column(name = "export_reason")
    private String exportReason;  // Lý do xuất (không bắt buộc)

    @OneToMany(mappedBy = "exportReceipt", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Book> book = new ArrayList<>();  // Sách đã xuất

}
