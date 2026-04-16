package com.awsome.shop.auth.infrastructure.security.api.service;

/**
 * 密码哈希服务接口
 */
public interface PasswordHashService {

    String hash(String rawPassword);

    boolean verify(String rawPassword, String hashedPassword);
}
