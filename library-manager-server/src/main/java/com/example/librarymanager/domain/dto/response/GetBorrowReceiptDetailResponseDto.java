package com.example.librarymanager.domain.dto.response;

import com.example.librarymanager.domain.entity.BookBorrow;
import com.example.librarymanager.domain.entity.BorrowReceipt;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class GetBorrowReceiptDetailResponseDto {

    @Getter
    private static class GetBookBorrowResponseDto {
        private final long id;
        private final String bookCode;
        private final LocalDate dueDate;
        private final boolean returned;

        public GetBookBorrowResponseDto(BookBorrow bookBorrow) {
            this.id = bookBorrow.getId();
            this.bookCode = bookBorrow.getBook().getBookCode();
            this.dueDate = bookBorrow.getDueDate();
            this.returned = bookBorrow.isReturned();
        }
    }

    private final long id;

    private final String receiptNumber;

    private final LocalDate createdDate;

    private final String note;

    private final long readerId;

    private final List<GetBookBorrowResponseDto> books;

    public GetBorrowReceiptDetailResponseDto(BorrowReceipt borrowReceipt) {
        this.id = borrowReceipt.getId();
        this.receiptNumber = borrowReceipt.getReceiptNumber();
        this.createdDate = borrowReceipt.getCreatedDate();
        this.note = borrowReceipt.getNote();
        this.readerId = borrowReceipt.getReader().getId();
        this.books = new ArrayList<>();
        for (BookBorrow bookBorrow : borrowReceipt.getBookBorrows()) {
            books.add(new GetBookBorrowResponseDto(bookBorrow));
        }
    }
}