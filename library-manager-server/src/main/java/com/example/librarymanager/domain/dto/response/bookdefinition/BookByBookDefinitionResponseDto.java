package com.example.librarymanager.domain.dto.response.bookdefinition;

import com.example.librarymanager.domain.dto.common.BaseEntityDto;
import com.example.librarymanager.domain.entity.BookAuthor;
import com.example.librarymanager.domain.entity.BookDefinition;
import com.example.librarymanager.domain.entity.ClassificationSymbol;
import com.example.librarymanager.domain.entity.Publisher;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BookByBookDefinitionResponseDto {
    private final long id;

    private final String title;

    private final String bookCode;

    private final String publishingYear;

    private final int totalBooks; // Tổng số sách

    private final int availableBooks; // Số sách đang trong thư viện

    private final int borrowedBooks; // Số sách đang mượn

    private final int lostBooks; // Số sách đã mất

    private final BaseEntityDto classificationSymbol;

    private final List<BaseEntityDto> authors = new ArrayList<>();

    private final BaseEntityDto publisher;

    public BookByBookDefinitionResponseDto(BookDefinition bookDefinition) {
        this.id = bookDefinition.getId();
        this.title = bookDefinition.getTitle();
        this.bookCode = bookDefinition.getBookCode();
        this.publishingYear = bookDefinition.getPublishingYear();
        this.availableBooks = 0;
        this.borrowedBooks = 0;
        this.lostBooks = 0;

        this.totalBooks = (int) bookDefinition.getBooks().stream()
                .filter(book -> book.getExportReceipt() == null)
                .count();

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
