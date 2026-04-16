package com.awsome.shop.auth.infrastructure.security.jwt;

import com.awsome.shop.auth.infrastructure.security.api.service.PasswordHashService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * BCrypt 密码哈希服务实现
 */
@Service
public class BcryptPasswordHashServiceImpl implements PasswordHashService {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String hash(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    @Override
    public boolean verify(String rawPassword, String hashedPassword) {
        return encoder.matches(rawPassword, hashedPassword);
    }
}
