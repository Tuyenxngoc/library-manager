package com.example.librarymanager.controller;

import com.example.librarymanager.annotation.RestApiV1;
import com.example.librarymanager.base.VsResponseUtil;
import com.example.librarymanager.constant.UrlConstant;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.request.CategoryRequestDto;
import com.example.librarymanager.service.CategoryService;
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
@Tag(name = "Category")
public class CategoryController {

    CategoryService categoryService;

    @Operation(summary = "API Create Category")
    @PostMapping(UrlConstant.Category.CREATE)
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequestDto requestDto) {
        return VsResponseUtil.success(HttpStatus.CREATED, categoryService.save(requestDto));
    }

    @Operation(summary = "API Update Category")
    @PutMapping(UrlConstant.Category.UPDATE)
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDto requestDto
    ) {
        return VsResponseUtil.success(categoryService.update(id, requestDto));
    }

    @Operation(summary = "API Delete Category")
    @DeleteMapping(UrlConstant.Category.DELETE)
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        return VsResponseUtil.success(categoryService.delete(id));
    }

    @Operation(summary = "API Get Categories")
    @GetMapping(UrlConstant.Category.GET_ALL)
    public ResponseEntity<?> getCategories(@ParameterObject PaginationFullRequestDto requestDto) {
        return VsResponseUtil.success(categoryService.findAll(requestDto));
    }

    @Operation(summary = "API Get Category By Id")
    @GetMapping(UrlConstant.Category.GET_BY_ID)
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        return VsResponseUtil.success(categoryService.findById(id));
    }

    @Operation(summary = "API Toggle Active Status of Category")
    @PatchMapping(UrlConstant.Category.TOGGLE_ACTIVE)
    public ResponseEntity<?> toggleActiveStatus(@PathVariable Long id) {
        return VsResponseUtil.success(categoryService.toggleActiveStatus(id));
    }
}