package com.awsome.shop.auth.domain.service.user;

import com.awsome.shop.auth.domain.model.user.UserEntity;

/**
 * 用户领域服务接口
 */
public interface UserDomainService {

    UserEntity register(String username, String passwordHash, String displayName);

    UserEntity authenticate(String username, String rawPassword);

    UserEntity findById(Long id);

    UserEntity findByUsername(String username);

    void update(UserEntity user);
}
