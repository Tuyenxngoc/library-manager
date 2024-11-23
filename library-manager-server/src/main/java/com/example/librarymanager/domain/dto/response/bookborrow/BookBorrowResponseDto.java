package com.example.librarymanager.domain.dto.response.bookborrow;

import com.example.librarymanager.domain.entity.BookBorrow;
import com.example.librarymanager.domain.entity.BorrowReceipt;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class BookBorrowResponseDto {

    private final long id;

    private final String bookCode;

    private final String title;

    private final String receiptNumber;

    private final String cardNumber;

    private final String fullName;

    private final LocalDate borrowDate;

    private final LocalDate dueDate;

    public BookBorrowResponseDto(BookBorrow bookBorrow) {
        BorrowReceipt borrowReceipt = bookBorrow.getBorrowReceipt();
        this.id = bookBorrow.getId();
        this.bookCode = bookBorrow.getBook().getBookCode();
        this.title = bookBorrow.getBook().getBookDefinition().getTitle();
        this.receiptNumber = borrowReceipt.getReceiptNumber();
        this.cardNumber = borrowReceipt.getReader().getCardNumber();
        this.fullName = borrowReceipt.getReader().getFullName();
        this.borrowDate = borrowReceipt.getBorrowDate();
        this.dueDate = borrowReceipt.getDueDate();
    }
}