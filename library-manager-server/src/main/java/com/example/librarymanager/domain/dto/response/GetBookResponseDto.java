package com.example.librarymanager.domain.dto.response;

import com.example.librarymanager.constant.BookCondition;
import com.example.librarymanager.domain.dto.common.DateAuditingDto;
import com.example.librarymanager.domain.entity.Book;
import lombok.Getter;

@Getter
public class GetBookResponseDto extends DateAuditingDto {

    private final long id;

    private final String bookCode;

    private final BookCondition bookCondition;

    private final GetBookDefinitionResponseDto bookDefinition;

    public GetBookResponseDto(Book book) {
        this.createdDate = book.getCreatedDate();
        this.lastModifiedDate = book.getLastModifiedDate();
        this.id = book.getId();
        this.bookCode = book.getBookCode();
        this.bookCondition = book.getBookCondition();
        this.bookDefinition = new GetBookDefinitionResponseDto(book.getBookDefinition());
    }
}
