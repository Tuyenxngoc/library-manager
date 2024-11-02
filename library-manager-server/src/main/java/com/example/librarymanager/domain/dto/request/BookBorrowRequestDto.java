package com.example.librarymanager.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookBorrowRequestDto {

    private String bookCode;

    private LocalDate dueDate;

}
