package com.example.librarymanager.domain.dto.response;

import com.example.librarymanager.domain.entity.BookSet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetBookSetResponseDto {

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    private Boolean deleteFlag;

    private Boolean activeFlag;

    private Long id;

    private String name;

    private int bookCount;

    public GetBookSetResponseDto(BookSet bookSet) {
        this.createdDate = bookSet.getCreatedDate();
        this.lastModifiedDate = bookSet.getLastModifiedDate();
        this.createdBy = bookSet.getCreatedBy();
        this.lastModifiedBy = bookSet.getLastModifiedBy();
        this.deleteFlag = bookSet.getDeleteFlag();
        this.activeFlag = bookSet.getActiveFlag();

        this.id = bookSet.getId();
        this.name = bookSet.getName();
        this.bookCount = bookSet.getBookDefinitions().size();
    }
}
