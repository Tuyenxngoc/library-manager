package com.example.librarymanager.domain.dto.response.bookdefinition;

import com.example.librarymanager.domain.dto.common.BaseEntityDto;
import com.example.librarymanager.domain.entity.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BookByBookDefinitionResponseDto {
    private final long id;

    private final String title;

    private final String bookCode;

    private final String publishingYear;

    private final long totalBooks; // Tổng số sách

    private final long availableBooks; // Số sách đang trong thư viện

    private final long borrowedBooks; // Số sách đang mượn

    private final long lostBooks; // Số sách đã mất

    private final BaseEntityDto classificationSymbol;

    private final List<BaseEntityDto> authors = new ArrayList<>();

    private final BaseEntityDto publisher;

    public BookByBookDefinitionResponseDto(BookDefinition bookDefinition) {
        this.id = bookDefinition.getId();
        this.title = bookDefinition.getTitle();
        this.bookCode = bookDefinition.getBookCode();
        this.publishingYear = bookDefinition.getPublishingYear();

        List<Book> books = bookDefinition.getBooks().stream()
                .filter(book -> book.getExportReceipt() == null)
                .toList();
        this.totalBooks = books.size();
        this.availableBooks = books.stream().filter(book -> {
            List<BookBorrow> bookBorrows = book.getBookBorrows();
            return bookBorrows.stream().allMatch(BookBorrow::isReturned);
        }).count();
        this.borrowedBooks = totalBooks - availableBooks;
        this.lostBooks = 0;

        // Set authors
        List<BookAuthor> au = bookDefinition.getBookAuthors();
        if (au != null) {
            this.authors.addAll(au.stream()
                    .map(BookAuthor::getAuthor)
                    .map(author -> new BaseEntityDto(author.getId(), author.getFullName()))
                    .toList());
        }

        // Set publisher
        Publisher p = bookDefinition.getPublisher();
        this.publisher = p != null ? new BaseEntityDto(p.getId(), p.getName()) : null;

        //Set classificationSymbol
        ClassificationSymbol c = bookDefinition.getClassificationSymbol();
        this.classificationSymbol = c != null ? new BaseEntityDto(c.getId(), c.getCode()) : null;
    }
}
