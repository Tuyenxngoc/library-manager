package com.example.librarymanager.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BorrowReceiptRequestDto {

    private String receiptNumber;

    private LocalDate createdDate;

    private String note;

    private Long readerId;

    private List<BookBorrowRequestDto> books = new ArrayList<>();

}
