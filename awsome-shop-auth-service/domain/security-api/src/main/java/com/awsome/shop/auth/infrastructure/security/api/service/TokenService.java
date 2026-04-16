package com.awsome.shop.auth.infrastructure.security.api.service;

import com.awsome.shop.auth.domain.model.auth.TokenClaims;

/**
 * JWT Token 服务接口
 */
public interface TokenService {

    String generateAccessToken(Long userId, String username, String role);

    String generateRefreshToken(Long userId);

    TokenClaims validateToken(String token);
}
