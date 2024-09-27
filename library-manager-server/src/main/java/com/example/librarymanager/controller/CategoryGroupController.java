package com.example.librarymanager.controller;

import com.example.librarymanager.annotation.RestApiV1;
import com.example.librarymanager.base.VsResponseUtil;
import com.example.librarymanager.constant.UrlConstant;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.request.CategoryGroupRequestDto;
import com.example.librarymanager.service.CategoryGroupService;
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
@Tag(name = "Category Group")
public class CategoryGroupController {

    CategoryGroupService categoryGroupService;

    @Operation(summary = "API Create Category Group")
    @PostMapping(UrlConstant.CategoryGroup.CREATE)
    public ResponseEntity<?> createCategoryGroup(@Valid @RequestBody CategoryGroupRequestDto requestDto) {
        return VsResponseUtil.success(HttpStatus.CREATED, categoryGroupService.save(requestDto));
    }

    @Operation(summary = "API Update Category Group")
    @PutMapping(UrlConstant.CategoryGroup.UPDATE)
    public ResponseEntity<?> updateCategoryGroup(
            @PathVariable Long id,
            @Valid @RequestBody CategoryGroupRequestDto requestDto
    ) {
        return VsResponseUtil.success(categoryGroupService.update(id, requestDto));
    }

    @Operation(summary = "API Delete Category Group")
    @DeleteMapping(UrlConstant.CategoryGroup.DELETE)
    public ResponseEntity<?> deleteCategoryGroup(@PathVariable Long id) {
        return VsResponseUtil.success(categoryGroupService.delete(id));
    }

    @Operation(summary = "API Get Category Groups")
    @GetMapping(UrlConstant.CategoryGroup.GET_ALL)
    public ResponseEntity<?> getCategoryGroups(@ParameterObject PaginationFullRequestDto requestDto) {
        return VsResponseUtil.success(categoryGroupService.findAll(requestDto));
    }

    @Operation(summary = "API Get Category Group By Id")
    @GetMapping(UrlConstant.CategoryGroup.GET_BY_ID)
    public ResponseEntity<?> getCategoryGroupById(@PathVariable Long id) {
        return VsResponseUtil.success(categoryGroupService.findById(id));
    }

    @Operation(summary = "API Toggle Active Status of Category Group")
    @PatchMapping(UrlConstant.CategoryGroup.TOGGLE_ACTIVE)
    public ResponseEntity<?> toggleActiveStatus(@PathVariable Long id) {
        return VsResponseUtil.success(categoryGroupService.toggleActiveStatus(id));
    }

}
