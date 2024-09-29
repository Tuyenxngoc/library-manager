package com.example.librarymanager.controller;

import com.example.librarymanager.annotation.CurrentUser;
import com.example.librarymanager.annotation.RestApiV1;
import com.example.librarymanager.base.VsResponseUtil;
import com.example.librarymanager.constant.UrlConstant;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.request.BookSetRequestDto;
import com.example.librarymanager.security.CustomUserDetails;
import com.example.librarymanager.service.BookSetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Book Set")
public class BookSetController {

    BookSetService bookSetService;

    @Operation(summary = "API Create Book Set")
    @PostMapping(UrlConstant.BookSet.CREATE)
    public ResponseEntity<?> createBookSet(
            @Valid @RequestBody BookSetRequestDto requestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(HttpStatus.CREATED, bookSetService.save(requestDto, userDetails.getUserId()));
    }

    @Operation(summary = "API Update Book Set")
    @PutMapping(UrlConstant.BookSet.UPDATE)
    public ResponseEntity<?> updateBookSet(
            @PathVariable Long id,
            @Valid @RequestBody BookSetRequestDto requestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(bookSetService.update(id, requestDto, userDetails.getUserId()));
    }

    @Operation(summary = "API Delete Book Set")
    @DeleteMapping(UrlConstant.BookSet.DELETE)
    public ResponseEntity<?> deleteBookSet(
            @PathVariable Long id,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(bookSetService.delete(id, userDetails.getUserId()));
    }

    @Operation(summary = "API Get All Book Sets")
    @GetMapping(UrlConstant.BookSet.GET_ALL)
    public ResponseEntity<?> getAllBookSets(@ParameterObject PaginationFullRequestDto requestDto) {
        return VsResponseUtil.success(bookSetService.findAll(requestDto));
    }

    @Operation(summary = "API Get Book Set By Id")
    @GetMapping(UrlConstant.BookSet.GET_BY_ID)
    public ResponseEntity<?> getBookSetById(@PathVariable Long id) {
        return VsResponseUtil.success(bookSetService.findById(id));
    }

    @Operation(summary = "API Toggle Active Status of Book Set")
    @PatchMapping(UrlConstant.BookSet.TOGGLE_ACTIVE)
    public ResponseEntity<?> toggleActiveStatus(
            @PathVariable Long id,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(bookSetService.toggleActiveStatus(id, userDetails.getUserId()));
    }
}
