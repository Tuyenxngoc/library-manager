package com.example.librarymanager.domain.dto.response.borrowreceipt;

import com.example.librarymanager.domain.entity.BookBorrow;
import com.example.librarymanager.domain.entity.BorrowReceipt;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BorrowReceiptDetailsDto {

    @Getter
    static class BookDto {
        private String title;
        private String bookCode;
        private boolean returned;

        public BookDto(BookBorrow book) {
            this.title = book.getBook().getBookDefinition().getTitle();
            this.bookCode = book.getBook().getBookCode();
            this.returned = book.isReturned();
        }
    }

    private Long id;
    private String receiptNumber;
    private String fullName;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;
    private List<BookDto> books;

    public BorrowReceiptDetailsDto(BorrowReceipt borrowReceipt) {
        this.id = borrowReceipt.getId();
        this.receiptNumber = borrowReceipt.getReceiptNumber();
        this.fullName = borrowReceipt.getReader().getFullName();
        this.borrowDate = borrowReceipt.getBorrowDate();
        this.dueDate = borrowReceipt.getDueDate();
        this.returnDate = borrowReceipt.getReturnDate();
        this.status = borrowReceipt.getStatus().getName();
        this.books = borrowReceipt.getBookBorrows().stream()
                .map(BookDto::new)
                .collect(Collectors.toList());
    }
}
