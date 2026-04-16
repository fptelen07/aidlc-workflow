package com.awsome.shop.auth.infrastructure.security.jwt;

import com.awsome.shop.auth.common.exception.BusinessException;
import com.awsome.shop.auth.domain.model.auth.TokenClaims;
import com.awsome.shop.auth.infrastructure.security.api.service.TokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT Token 服务实现
 */
@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements TokenService {

    private final JwtProperties jwtProperties;

    @Override
    public String generateAccessToken(Long userId, String username, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.getAccessTokenExpireSeconds() * 1000);
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("username", username)
                .claim("role", role)
                .claim("type", "access")
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    @Override
    public String generateRefreshToken(Long userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.getRefreshTokenExpireSeconds() * 1000);
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("type", "refresh")
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    @Override
    public TokenClaims validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            TokenClaims tc = new TokenClaims();
            tc.setUserId(Long.parseLong(claims.getSubject()));
            tc.setUsername(claims.get("username", String.class));
            tc.setRole(claims.get("role", String.class));
            tc.setType(claims.get("type", String.class));
            return tc;
        } catch (ExpiredJwtException e) {
            throw new BusinessException("AUTH_002", "Token 已过期，请重新登录");
        } catch (JwtException e) {
            throw new BusinessException("AUTH_003", "无效的 Token");
        }
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
