package com.example.librarymanager.domain.entity;

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
@Table(name = "borrow_receipts")
public class BorrowReceipt {//Phiếu mượn sách

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "borrow_receipt_id")
    private Long id;

    @Column(name = "loan_date", nullable = false)
    private LocalDate loanDate; // Ngày mượn

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
