package com.example.librarymanager.domain.dto.request;

import com.example.librarymanager.constant.ErrorMessage;
import com.example.librarymanager.domain.entity.BookBorrow;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookBorrowRequestDto {

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Size(max = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String bookCode;

    @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
    private LocalDate dueDate;

    public BookBorrowRequestDto(String bookCode) {
        this.bookCode = bookCode;
    }

    public BookBorrowRequestDto(BookBorrow bookBorrow) {
        this.bookCode = bookBorrow.getBook().getBookCode();
        this.dueDate = bookBorrow.getDueDate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookBorrowRequestDto dto = (BookBorrowRequestDto) o;
        return Objects.equals(bookCode, dto.bookCode);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(bookCode);
    }

}
