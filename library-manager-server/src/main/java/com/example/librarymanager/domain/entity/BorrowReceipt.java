package com.example.librarymanager.domain.entity;

import com.example.librarymanager.constant.BorrowStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "borrow_receipts",
        uniqueConstraints = @UniqueConstraint(name = "UN_BOOK_RECEIPTS_RECEIPT_NUMBER", columnNames = "receipt_number"))
public class BorrowReceipt {//Phiếu mượn sách

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "borrow_receipt_id")
    private Long id;

    @Column(name = "receipt_number", nullable = false)
    private String receiptNumber; //Số phiếu mượn

    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate; // Ngày mượn

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate; // Ngày hẹn trả

    @Column(name = "return_date")
    private LocalDate returnDate; // Ngày trả thực tế

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BorrowStatus status;

    @Column(name = "note", length = 500)
    private String note; // Ghi chú

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reader_id", foreignKey = @ForeignKey(name = "FK_BOOK_LOAN_RECEIPTS_READER_ID"), referencedColumnName = "reader_id", nullable = false)
    @JsonIgnore
    private Reader reader;

    @OneToMany(mappedBy = "borrowReceipt", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BookBorrow> bookBorrows = new ArrayList<>();//Các sách mượn

}
