package com.example.librarymanager.domain.dto.request;

import com.example.librarymanager.constant.CardType;
import com.example.librarymanager.constant.CommonConstant;
import com.example.librarymanager.constant.ErrorMessage;
import com.example.librarymanager.constant.Gender;
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
    private CardType cardType; // Loại thẻ

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Size(max = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String fullName; // Họ tên

    @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
    private LocalDate dateOfBirth; // Ngày sinh

    private Gender gender; // Giới tính (Nam, Nữ)

    @Size(max = 255, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String address; // Địa chỉ

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Pattern(regexp = CommonConstant.REGEXP_PHONE_NUMBER, message = ErrorMessage.INVALID_FORMAT_PHONE)
    @Size(min = 10, max = 20, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String phoneNumber; // Số điện thoại

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Size(max = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String cardNumber; // Số thẻ

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Size(max = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String password; // Mật khẩu

    private LocalDate expiryDate; // Ngày hết hạn
}
