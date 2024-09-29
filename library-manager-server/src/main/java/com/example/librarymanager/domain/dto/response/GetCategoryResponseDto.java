package com.example.librarymanager.domain.dto.response;

import com.example.librarymanager.domain.entity.Category;
import com.example.librarymanager.domain.entity.CategoryGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetCategoryResponseDto {

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    private Boolean activeFlag;

    private Long id;

    private String categoryName;

    private String categoryCode;

    private BaseEntityDto categoryGroup;

    public GetCategoryResponseDto(Category category) {
        this.createdDate = category.getCreatedDate();
        this.lastModifiedDate = category.getLastModifiedDate();
        this.createdBy = category.getCreatedBy();
        this.lastModifiedBy = category.getLastModifiedBy();
        this.activeFlag = category.getActiveFlag();

        this.id = category.getId();
        this.categoryName = category.getCategoryName();
        this.categoryCode = category.getCategoryCode();

        CategoryGroup group = category.getCategoryGroup();
        this.categoryGroup = new BaseEntityDto(group.getId(), group.getGroupName());
    }
}
