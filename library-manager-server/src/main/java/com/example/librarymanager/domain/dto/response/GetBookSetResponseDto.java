package com.example.librarymanager.domain.dto.response;

import com.example.librarymanager.domain.entity.BookSet;
import lombok.Getter;

@Getter
public class GetBookSetResponseDto {

    private final Long id;

    private final String name;

    private final Boolean activeFlag;

    private final int bookCount;

    public GetBookSetResponseDto(BookSet bookSet) {
        this.id = bookSet.getId();
        this.name = bookSet.getName();
        this.activeFlag = bookSet.getActiveFlag();
        this.bookCount = bookSet.getBookDefinitions().size();
    }
}
