package com.example.librarymanager.domain.dto.response;

import com.example.librarymanager.domain.entity.Category;
import com.example.librarymanager.domain.entity.CategoryGroup;
import lombok.Getter;

@Getter
public class GetCategoryResponseDto {

    private final Long id;

    private final String categoryName;

    private final String categoryCode;

    private final Boolean activeFlag;

    private final BaseEntityDto categoryGroup;

    public GetCategoryResponseDto(Category category) {
        this.id = category.getId();
        this.categoryName = category.getCategoryName();
        this.categoryCode = category.getCategoryCode();
        this.activeFlag = category.getActiveFlag();
        CategoryGroup group = category.getCategoryGroup();
        this.categoryGroup = new BaseEntityDto(group.getId(), group.getGroupName());
    }
}
