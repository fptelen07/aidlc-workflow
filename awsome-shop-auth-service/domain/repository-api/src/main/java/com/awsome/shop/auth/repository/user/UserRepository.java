package com.awsome.shop.auth.repository.user;

import com.awsome.shop.auth.domain.model.user.UserEntity;

/**
 * 用户仓储接口
 */
public interface UserRepository {

    UserEntity save(UserEntity user);

    UserEntity findById(Long id);

    UserEntity findByUsername(String username);

    boolean existsByUsername(String username);

    void update(UserEntity user);
}
