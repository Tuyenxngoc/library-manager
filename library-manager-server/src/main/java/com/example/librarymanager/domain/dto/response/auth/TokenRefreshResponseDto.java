package com.example.librarymanager.domain.dto.response.auth;

import com.example.librarymanager.constant.CommonConstant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TokenRefreshResponseDto {

    private String tokenType = CommonConstant.TOKEN_TYPE;

    private String accessToken;

    private String refreshToken;

    public TokenRefreshResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
