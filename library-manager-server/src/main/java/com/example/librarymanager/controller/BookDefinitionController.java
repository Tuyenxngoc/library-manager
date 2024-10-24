package com.example.librarymanager.controller;

import com.example.librarymanager.annotation.CurrentUser;
import com.example.librarymanager.annotation.RestApiV1;
import com.example.librarymanager.base.VsResponseUtil;
import com.example.librarymanager.constant.UrlConstant;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.request.BookDefinitionRequestDto;
import com.example.librarymanager.security.CustomUserDetails;
import com.example.librarymanager.service.BookDefinitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Book Definition")
public class BookDefinitionController {

    BookDefinitionService bookDefinitionService;

    @Operation(summary = "API Create Book Definition")
    @PreAuthorize("hasRole('ROLE_MANAGE_BOOK_DEFINITION')")
    @PostMapping(value = UrlConstant.BookDefinition.CREATE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createBookDefinition(
            @Valid @ModelAttribute BookDefinitionRequestDto requestDto,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(HttpStatus.CREATED, bookDefinitionService.save(requestDto, image, userDetails.getUserId()));
    }

    @Operation(summary = "API Update Book Definition")
    @PreAuthorize("hasRole('ROLE_MANAGE_BOOK_DEFINITION')")
    @PutMapping(value = UrlConstant.BookDefinition.UPDATE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateBookDefinition(
            @PathVariable Long id,
            @Valid @ModelAttribute BookDefinitionRequestDto requestDto,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(bookDefinitionService.update(id, requestDto, image, userDetails.getUserId()));
    }

    @Operation(summary = "API Delete Book Definition")
    @PreAuthorize("hasRole('ROLE_MANAGE_BOOK_DEFINITION')")
    @DeleteMapping(UrlConstant.BookDefinition.DELETE)
    public ResponseEntity<?> deleteBookDefinition(
            @PathVariable Long id,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(bookDefinitionService.delete(id, userDetails.getUserId()));
    }

    @Operation(summary = "API Get All Book Definitions")
    @PreAuthorize("hasAnyRole('ROLE_MANAGE_BOOK_DEFINITION', 'ROLE_MANAGE_IMPORT_RECEIPT')")
    @GetMapping(UrlConstant.BookDefinition.GET_ALL)
    public ResponseEntity<?> getAllBookDefinitions(@ParameterObject PaginationFullRequestDto requestDto) {
        return VsResponseUtil.success(bookDefinitionService.findAll(requestDto));
    }

    @Operation(summary = "API Get Book Definition By Id")
    @PreAuthorize("hasRole('ROLE_MANAGE_BOOK_DEFINITION')")
    @GetMapping(UrlConstant.BookDefinition.GET_BY_ID)
    public ResponseEntity<?> getBookDefinitionById(@PathVariable Long id) {
        return VsResponseUtil.success(bookDefinitionService.findById(id));
    }

    @Operation(summary = "API Toggle Active Status of Book Definition")
    @PreAuthorize("hasRole('ROLE_MANAGE_BOOK_DEFINITION')")
    @PatchMapping(UrlConstant.BookDefinition.TOGGLE_ACTIVE)
    public ResponseEntity<?> toggleActiveStatus(
            @PathVariable Long id,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(bookDefinitionService.toggleActiveStatus(id, userDetails.getUserId()));
    }

    @Operation(summary = "API Get Books")
    @PreAuthorize("hasRole('ROLE_MANAGE_BOOK')")
    @GetMapping(UrlConstant.BookDefinition.GET_BOOKS)
    public ResponseEntity<?> getBooks(
            @ParameterObject PaginationFullRequestDto requestDto,
            @RequestParam(value = "categoryGroupId", required = false) Long categoryGroupId,
            @RequestParam(value = "categoryId", required = false) Long categoryId
    ) {
        return VsResponseUtil.success(bookDefinitionService.getBooks(requestDto, categoryGroupId, categoryId));
    }

    @Operation(summary = "API Get Books For User")
    @GetMapping(UrlConstant.BookDefinition.GET_BOOKS_FOR_USER)
    public ResponseEntity<?> getBooksForUser(
            @ParameterObject PaginationFullRequestDto requestDto,
            @RequestParam(value = "categoryGroupId", required = false) Long categoryGroupId,
            @RequestParam(value = "categoryId", required = false) Long categoryId
    ) {
        return VsResponseUtil.success(bookDefinitionService.getBooksForUser(requestDto, categoryGroupId, categoryId));
    }
}
