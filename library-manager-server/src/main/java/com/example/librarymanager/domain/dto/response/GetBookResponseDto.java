package com.example.librarymanager.domain.dto.response;

import com.example.librarymanager.constant.BookCondition;
import com.example.librarymanager.domain.entity.Book;
import lombok.Getter;

@Getter
public class GetBookResponseDto {

    private final long id;

    private final String bookCode; // Số đăng ký cá biệt sách

    private final BookCondition bookCondition;

    private final GetBookDefinitionResponseDto bookDefinition;

    public GetBookResponseDto(Book book) {
        this.id = book.getId();
        this.bookCode = book.getBookCode();
        this.bookCondition = book.getBookCondition();
        this.bookDefinition = new GetBookDefinitionResponseDto(book.getBookDefinition());
    }
}
