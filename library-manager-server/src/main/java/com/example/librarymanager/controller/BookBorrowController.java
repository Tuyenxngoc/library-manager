package com.example.librarymanager.controller;

import com.example.librarymanager.annotation.CurrentUser;
import com.example.librarymanager.annotation.RestApiV1;
import com.example.librarymanager.base.VsResponseUtil;
import com.example.librarymanager.constant.ErrorMessage;
import com.example.librarymanager.constant.UrlConstant;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.security.CustomUserDetails;
import com.example.librarymanager.service.BookBorrowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Book Borrow")
public class BookBorrowController {

    BookBorrowService bookBorrowService;

    @Operation(summary = "API Return Books by List of IDs")
    @PreAuthorize("hasRole('ROLE_MANAGE_BORROW_RECEIPT')")
    @PutMapping(UrlConstant.BookBorrow.RETURN_BOOKS)
    public ResponseEntity<?> returnBooks(
            @RequestBody
            @NotNull(message = ErrorMessage.INVALID_ARRAY_IS_REQUIRED)
            Set<@NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED) Long> ids,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(bookBorrowService.returnBooksByIds(ids, userDetails.getUserId()));
    }

    @Operation(summary = "API Get All Book Borrows")
    @PreAuthorize("hasRole('ROLE_MANAGE_BORROW_RECEIPT')")
    @GetMapping(UrlConstant.BookBorrow.GET_ALL)
    public ResponseEntity<?> getAllBookBorrows(@ParameterObject PaginationFullRequestDto requestDto) {
        return VsResponseUtil.success(bookBorrowService.findAll(requestDto));
    }

}