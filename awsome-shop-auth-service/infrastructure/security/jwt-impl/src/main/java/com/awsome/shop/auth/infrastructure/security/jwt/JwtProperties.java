package com.awsome.shop.auth.infrastructure.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secret = "awsome-shop-jwt-secret-key-must-be-at-least-256-bits-long-for-hs256";
    private long accessTokenExpireSeconds = 7200;       // 2 hours
    private long refreshTokenExpireSeconds = 604800;    // 7 days
}
