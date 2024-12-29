package com.example.librarymanager.domain.dto.response.bookdefinition;

import com.example.librarymanager.domain.dto.common.BaseEntityDto;
import com.example.librarymanager.domain.entity.BookAuthor;
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
        this.quantity = 0;

        List<BookAuthor> au = bookDefinition.getBookAuthors();
        if (au != null) {
            this.authors.addAll(au.stream()
                    .map(BookAuthor::getAuthor)
                    .map(author -> new BaseEntityDto(author.getId(), author.getFullName()))
                    .toList());
        }
    }

}
