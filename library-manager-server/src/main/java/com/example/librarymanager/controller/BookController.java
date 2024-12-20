package com.example.librarymanager.controller;

import com.example.librarymanager.annotation.RestApiV1;
import com.example.librarymanager.base.VsResponseUtil;
import com.example.librarymanager.constant.UrlConstant;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Book")
public class BookController {

    BookService bookService;

    @Operation(summary = "API Get All Books")
    @PreAuthorize("hasAnyRole('ROLE_MANAGE_BOOK', 'ROLE_MANAGE_EXPORT_RECEIPT')")
    @GetMapping(UrlConstant.Book.GET_ALL)
    public ResponseEntity<?> getAllBooks(@ParameterObject PaginationFullRequestDto requestDto) {
        return VsResponseUtil.success(bookService.findAll(requestDto));
    }

    @Operation(summary = "API Get Book By List of IDs")
    @PreAuthorize("hasAnyRole('ROLE_MANAGE_BOOK', 'ROLE_MANAGE_EXPORT_RECEIPT')")
    @PostMapping(UrlConstant.Book.GET_BY_IDS)
    public ResponseEntity<?> getBooksByIds(@RequestBody Set<Long> ids) {
        return VsResponseUtil.success(bookService.findByIds(ids));
    }

    @Operation(summary = "API Get Book By Id")
    @PreAuthorize("hasRole('ROLE_MANAGE_BOOK')")
    @GetMapping(UrlConstant.Book.GET_BY_ID)
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        return VsResponseUtil.success(bookService.findById(id));
    }
}
