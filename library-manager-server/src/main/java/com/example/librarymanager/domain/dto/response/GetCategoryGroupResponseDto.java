package com.example.librarymanager.domain.dto.response;

import com.example.librarymanager.domain.entity.CategoryGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetCategoryGroupResponseDto {

    private long id;

    private String groupName;

    public GetCategoryGroupResponseDto(CategoryGroup categoryGroup) {
        this.id = categoryGroup.getId();
        this.groupName = categoryGroup.getGroupName();
    }

}
