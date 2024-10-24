package com.example.librarymanager.domain.dto.response;

import com.example.librarymanager.domain.entity.BookDefinition;
import lombok.Getter;

@Getter
public class GetBookForUserResponseDto {

    private final long id;

    private final String title;

    private final String imageUrl;

    private final int quantity;

    public GetBookForUserResponseDto(BookDefinition bookDefinition) {
        this.id = bookDefinition.getId();
        this.title = bookDefinition.getTitle();
        this.imageUrl = bookDefinition.getImageUrl();
        this.quantity = bookDefinition.getBooks().size();//todo
    }

}
