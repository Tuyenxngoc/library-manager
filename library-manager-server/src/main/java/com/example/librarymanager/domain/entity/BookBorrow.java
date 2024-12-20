package com.example.librarymanager.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book_borrows")
public class BookBorrow {//Sách mượn chi tiết

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_borrow_id")
    private Long id;

    @Column(name = "returned", nullable = false)
    private boolean returned = false; // Trạng thái đã trả

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", foreignKey = @ForeignKey(name = "FK_BOOK_BORROWS_BOOK_ID"), referencedColumnName = "book_id", nullable = false)
    @JsonIgnore
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrow_receipt_id", foreignKey = @ForeignKey(name = "FK_BOOK_BORROWS_BORROWS_RECEIPT_ID"), referencedColumnName = "borrow_receipt_id", nullable = false)
    @JsonIgnore
    private BorrowReceipt borrowReceipt;

}
