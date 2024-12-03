package com.example.librarymanager.domain.dto.response.bookdefinition;

import com.example.librarymanager.domain.dto.common.BaseEntityDto;
import com.example.librarymanager.domain.entity.Book;
import com.example.librarymanager.domain.entity.BookAuthor;
import com.example.librarymanager.domain.entity.BookBorrow;
import com.example.librarymanager.domain.entity.BookDefinition;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BookForReaderResponseDto {

    private final long id;

    private final String title;

    private final String imageUrl;

    private final int quantity;

    private final List<BaseEntityDto> authors = new ArrayList<>();

    public BookForReaderResponseDto(BookDefinition bookDefinition) {
        this.id = bookDefinition.getId();
        this.title = bookDefinition.getTitle();
        this.imageUrl = bookDefinition.getImageUrl();

        int count = 0;
        List<Book> books = bookDefinition.getBooks();
        for (Book book : books) {
            boolean isReturn = book.getBookBorrows()
                    .stream()
                    .allMatch(BookBorrow::isReturned);
            if (isReturn) {
                count++;
            }
        }
        this.quantity = count;

        List<BookAuthor> au = bookDefinition.getBookAuthors();
        if (au != null) {
            this.authors.addAll(au.stream()
                    .map(BookAuthor::getAuthor)
                    .map(author -> new BaseEntityDto(author.getId(), author.getFullName()))
                    .toList());
        }
    }

}
