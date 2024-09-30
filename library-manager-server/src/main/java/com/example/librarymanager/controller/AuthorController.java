package com.example.librarymanager.controller;

import com.example.librarymanager.annotation.RestApiV1;
import com.example.librarymanager.base.VsResponseUtil;
import com.example.librarymanager.constant.UrlConstant;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.request.AuthorRequestDto;
import com.example.librarymanager.service.AuthorService;
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
@Tag(name = "Author")
public class AuthorController {

    AuthorService authorService;

    @Operation(summary = "API Create Author")
    @PostMapping(UrlConstant.Author.CREATE)
    public ResponseEntity<?> createAuthor(@Valid @RequestBody AuthorRequestDto requestDto) {
        return VsResponseUtil.success(HttpStatus.CREATED, authorService.save(requestDto));
    }

    @Operation(summary = "API Update Author")
    @PutMapping(UrlConstant.Author.UPDATE)
    public ResponseEntity<?> updateAuthor(
            @PathVariable Long id,
            @Valid @RequestBody AuthorRequestDto requestDto
    ) {
        return VsResponseUtil.success(authorService.update(id, requestDto));
    }

    @Operation(summary = "API Delete Author")
    @DeleteMapping(UrlConstant.Author.DELETE)
    public ResponseEntity<?> deleteAuthor(@PathVariable Long id) {
        return VsResponseUtil.success(authorService.delete(id));
    }

    @Operation(summary = "API Get Authors")
    @GetMapping(UrlConstant.Author.GET_ALL)
    public ResponseEntity<?> getAuthors(@ParameterObject PaginationFullRequestDto requestDto) {
        return VsResponseUtil.success(authorService.findAll(requestDto));
    }

    @Operation(summary = "API Get Author By Id")
    @GetMapping(UrlConstant.Author.GET_BY_ID)
    public ResponseEntity<?> getAuthorById(@PathVariable Long id) {
        return VsResponseUtil.success(authorService.findById(id));
    }

    @Operation(summary = "API Toggle Active Status of Author")
    @PatchMapping(UrlConstant.Author.TOGGLE_ACTIVE)
    public ResponseEntity<?> toggleActiveStatus(@PathVariable Long id) {
        return VsResponseUtil.success(authorService.toggleActiveStatus(id));
    }
}
