package com.example.librarymanager.service.impl;

import com.example.librarymanager.service.JwtTokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtTokenServiceImpl implements JwtTokenService {

    static final String ACCESS_TOKEN_KEY = "ACCESS_TOKEN_";
    static final String REFRESH_TOKEN_KEY = "REFRESH_TOKEN_";

    @Value("${jwt.access.expiration_time:60}")
    int expirationTimeAccessToken;

    @Value("${jwt.refresh.expiration_time:1440}")
    int expirationTimeRefreshToken;

    final RedisTemplate<String, Object> redisTemplate;

    private String getAccessTokenKey(String userId) {
        return ACCESS_TOKEN_KEY + userId.toUpperCase();
    }

    private String getRefreshTokenKey(String userId) {
        return REFRESH_TOKEN_KEY + userId.toUpperCase();
    }

    @Override
    public void saveAccessToken(String accessToken, String userId) {
        redisTemplate.opsForValue().set(getAccessTokenKey(userId), accessToken, expirationTimeAccessToken, TimeUnit.MINUTES);
    }

    @Override
    public void saveRefreshToken(String refreshToken, String userId) {
        redisTemplate.opsForValue().set(getRefreshTokenKey(userId), refreshToken, expirationTimeRefreshToken, TimeUnit.MINUTES);
    }

    @Override
    public boolean isAccessTokenExists(String accessToken, String userId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(getAccessTokenKey(userId))) &&
                accessToken.equals(redisTemplate.opsForValue().get(getAccessTokenKey(userId)));
    }

    @Override
    public boolean isRefreshTokenExists(String refreshToken, String userId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(getRefreshTokenKey(userId))) &&
                refreshToken.equals(redisTemplate.opsForValue().get(getRefreshTokenKey(userId)));
    }

    @Override
    public void deleteTokens(String userId) {
        redisTemplate.delete(getAccessTokenKey(userId));
        redisTemplate.delete(getRefreshTokenKey(userId));
    }
}
