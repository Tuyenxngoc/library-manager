package com.example.librarymanager.domain.dto.request.auth;

import com.example.librarymanager.constant.ErrorMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenRefreshRequestDto {

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Size(max = 500, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String refreshToken;

}