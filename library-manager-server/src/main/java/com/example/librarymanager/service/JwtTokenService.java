package com.example.librarymanager.service;

public interface JwtTokenService {

    void saveAccessToken(String accessToken, String userIdOrCardNumber);

    void saveRefreshToken(String refreshToken, String userIdOrCardNumber);

    boolean isAccessTokenExists(String accessToken, String userIdOrCardNumber);

    boolean isRefreshTokenExists(String refreshToken, String userIdOrCardNumber);

    void deleteTokens(String userIdOrCardNumber);

}
