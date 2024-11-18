package com.example.librarymanager.domain.dto.response.borrowreceipt;

import com.example.librarymanager.domain.entity.BorrowReceipt;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class BorrowReceiptResponseDto {
    private final long id;

    private final String receiptNumber;

    private final LocalDate borrowDate; //Ngày mượn

    private final LocalDate dueDate; // Ngày hết hạn

    private final LocalDate returnDate;

    private final String status;

    private final String note; // Ghi chú

    private final String cardNumber; // Số thẻ

    private final String fullName; // Họ tên

    private final int books;//Số sách mượn

    public BorrowReceiptResponseDto(BorrowReceipt borrowReceipt) {
        this.id = borrowReceipt.getId();
        this.receiptNumber = borrowReceipt.getReceiptNumber();
        this.borrowDate = borrowReceipt.getBorrowDate();
        this.dueDate = borrowReceipt.getDueDate();
        this.returnDate = borrowReceipt.getReturnDate();
        this.status = borrowReceipt.getStatus().getName();
        this.note = borrowReceipt.getNote();
        this.cardNumber = borrowReceipt.getReader().getCardNumber();
        this.fullName = borrowReceipt.getReader().getFullName();
        this.books = borrowReceipt.getBookBorrows().size();
    }
}
