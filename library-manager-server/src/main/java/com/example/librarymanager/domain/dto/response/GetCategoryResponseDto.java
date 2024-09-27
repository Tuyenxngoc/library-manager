package com.example.librarymanager.domain.dto.response;

import com.example.librarymanager.domain.entity.Category;
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

    private Boolean deleteFlag;

    private Boolean activeFlag;

    private Long id;

    private String categoryName;

    private String categoryCode;

    private GetCategoryGroupResponseDto categoryGroup;

    public GetCategoryResponseDto(Category category) {
        this.createdDate = category.getCreatedDate();
        this.lastModifiedDate = category.getLastModifiedDate();
        this.createdBy = category.getCreatedBy();
        this.lastModifiedBy = category.getLastModifiedBy();
        this.deleteFlag = category.getDeleteFlag();
        this.activeFlag = category.getActiveFlag();

        this.id = category.getId();
        this.categoryName = category.getCategoryName();
        this.categoryCode = category.getCategoryCode();
        this.categoryGroup = new GetCategoryGroupResponseDto(category.getCategoryGroup());
    }
}
