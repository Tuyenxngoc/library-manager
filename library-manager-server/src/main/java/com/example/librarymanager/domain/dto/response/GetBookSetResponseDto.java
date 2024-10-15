package com.example.librarymanager.domain.dto.response;

import com.example.librarymanager.domain.dto.common.DateAuditingDto;
import com.example.librarymanager.domain.entity.BookSet;
import lombok.Getter;

@Getter
public class GetBookSetResponseDto extends DateAuditingDto {

    private final Boolean activeFlag;

    private final Long id;

    private final String name;

    private final int bookCount;

    public GetBookSetResponseDto(BookSet bookSet) {
        this.createdDate = bookSet.getCreatedDate();
        this.lastModifiedDate = bookSet.getLastModifiedDate();
        this.activeFlag = bookSet.getActiveFlag();
        this.id = bookSet.getId();
        this.name = bookSet.getName();
        this.bookCount = bookSet.getBookDefinitions().size();
    }
}
