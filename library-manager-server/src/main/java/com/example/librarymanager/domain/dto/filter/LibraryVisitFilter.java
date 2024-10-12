package com.example.librarymanager.domain.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LibraryVisitFilter {
    private LocalDate startDate;

    private LocalDate endDate;
}
