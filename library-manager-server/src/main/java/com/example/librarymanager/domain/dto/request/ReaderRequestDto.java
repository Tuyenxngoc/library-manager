package com.example.librarymanager.domain.dto.request;

import com.example.librarymanager.constant.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReaderRequestDto {
    @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
    private CardType cardType;

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Pattern(regexp = CommonConstant.REGEXP_FULL_NAME, message = ErrorMessage.INVALID_FORMAT_NAME)
    @Size(min = 2, max = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String fullName;

    @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
    private LocalDate dateOfBirth;

    @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
    private Gender gender;

    @Size(max = 255, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String address;

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Pattern(regexp = CommonConstant.REGEXP_PHONE_NUMBER, message = ErrorMessage.INVALID_FORMAT_PHONE)
    @Size(min = 10, max = 20, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String phoneNumber;

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Size(max = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String cardNumber;

    private String password;

    @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
    private LocalDate expiryDate;

    @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
    private CardStatus status;
}
