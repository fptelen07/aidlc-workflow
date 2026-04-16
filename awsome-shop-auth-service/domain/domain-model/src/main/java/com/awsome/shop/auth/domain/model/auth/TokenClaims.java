package com.awsome.shop.auth.domain.model.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JWT Token Claims 值对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenClaims {

    private Long userId;
    private String username;
    private String role;
    private String type;  // "access" | "refresh"
}
