package com.example.librarymanager.domain.dto.response;

import com.example.librarymanager.domain.entity.BorrowReceipt;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class GetBorrowReceiptResponseDto {
    private final long id;

    private final String receiptNumber;

    private final LocalDate createdDate;

    private final String note; // Ghi chú

    private final String cardNumber; // Số thẻ

    private final String fullName; // Họ tên

    private final int books;//Các sách mượn

    public GetBorrowReceiptResponseDto(BorrowReceipt borrowReceipt) {
        this.id = borrowReceipt.getId();
        this.receiptNumber = borrowReceipt.getReceiptNumber();
        this.createdDate = borrowReceipt.getCreatedDate();
        this.note = borrowReceipt.getNote();
        this.cardNumber = borrowReceipt.getReader().getCardNumber();
        this.fullName = borrowReceipt.getReader().getFullName();
        this.books = borrowReceipt.getBookBorrows().size();
    }
}
