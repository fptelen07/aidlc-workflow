package com.awsome.shop.auth.domain.model.user;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户聚合根
 */
@Data
public class UserEntity {

    private Long id;
    private String username;
    private String passwordHash;
    private String displayName;
    private String role;        // "employee" | "admin"
    private String avatar;
    private String status;      // "active" | "disabled"
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
