package com.example.librarymanager.controller;

import com.example.librarymanager.annotation.RestApiV1;
import com.example.librarymanager.base.VsResponseUtil;
import com.example.librarymanager.constant.UrlConstant;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.request.BookDefinitionRequestDto;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Book Definition")
public class BookDefinitionController {

    BookDefinitionService bookDefinitionService;

    @Operation(summary = "API Create Book Definition")
    @PostMapping(value = UrlConstant.BookDefinition.CREATE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createBookDefinition(
            @Valid @ModelAttribute BookDefinitionRequestDto requestDto,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        return VsResponseUtil.success(HttpStatus.CREATED, bookDefinitionService.save(requestDto, image));
    }

    @Operation(summary = "API Update Book Definition")
    @PutMapping(UrlConstant.BookDefinition.UPDATE)
    public ResponseEntity<?> updateBookDefinition(
            @PathVariable Long id,
            @Valid @RequestBody BookDefinitionRequestDto requestDto
    ) {
        return VsResponseUtil.success(bookDefinitionService.update(id, requestDto));
    }

    @Operation(summary = "API Delete Book Definition")
    @DeleteMapping(UrlConstant.BookDefinition.DELETE)
    public ResponseEntity<?> deleteBookDefinition(@PathVariable Long id) {
        return VsResponseUtil.success(bookDefinitionService.delete(id));
    }

    @Operation(summary = "API Get All Book Definitions")
    @GetMapping(UrlConstant.BookDefinition.GET_ALL)
    public ResponseEntity<?> getAllBookDefinitions(@ParameterObject PaginationFullRequestDto requestDto) {
        return VsResponseUtil.success(bookDefinitionService.findAll(requestDto));
    }

    @Operation(summary = "API Get Book Definition By Id")
    @GetMapping(UrlConstant.BookDefinition.GET_BY_ID)
    public ResponseEntity<?> getBookDefinitionById(@PathVariable Long id) {
        return VsResponseUtil.success(bookDefinitionService.findById(id));
    }

    @Operation(summary = "API Toggle Active Status of Book Definition")
    @PatchMapping(UrlConstant.BookDefinition.TOGGLE_ACTIVE)
    public ResponseEntity<?> toggleActiveStatus(@PathVariable Long id) {
        return VsResponseUtil.success(bookDefinitionService.toggleActiveStatus(id));
    }
}
