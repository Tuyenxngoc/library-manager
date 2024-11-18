package com.example.librarymanager.domain.dto.response.bookdefinition;

import com.example.librarymanager.domain.entity.BookDefinition;
import lombok.Getter;

@Getter
public class BookForReaderResponseDto {

    private final long id;

    private final String title;

    private final String imageUrl;

    private final int quantity;

    public BookForReaderResponseDto(BookDefinition bookDefinition) {
        this.id = bookDefinition.getId();
        this.title = bookDefinition.getTitle();
        this.imageUrl = bookDefinition.getImageUrl();
        this.quantity = bookDefinition.getBooks().size();//todo
    }

}
