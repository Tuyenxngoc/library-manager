package com.example.librarymanager.domain.dto.response;

import com.example.librarymanager.domain.dto.common.DateAuditingDto;
import com.example.librarymanager.domain.entity.Category;
import com.example.librarymanager.domain.entity.CategoryGroup;
import lombok.Getter;

@Getter
public class GetCategoryResponseDto extends DateAuditingDto {

    private final Boolean activeFlag;

    private final Long id;

    private final String categoryName;

    private final String categoryCode;

    private final BaseEntityDto categoryGroup;

    public GetCategoryResponseDto(Category category) {
        this.createdDate = category.getCreatedDate();
        this.lastModifiedDate = category.getLastModifiedDate();
        this.activeFlag = category.getActiveFlag();
        this.id = category.getId();
        this.categoryName = category.getCategoryName();
        this.categoryCode = category.getCategoryCode();
        CategoryGroup group = category.getCategoryGroup();
        this.categoryGroup = new BaseEntityDto(group.getId(), group.getGroupName());
    }
}
