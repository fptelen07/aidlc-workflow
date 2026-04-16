package com.awsome.shop.auth.domain.impl.service.user;

import com.awsome.shop.auth.common.exception.BusinessException;
import com.awsome.shop.auth.domain.model.user.UserEntity;
import com.awsome.shop.auth.domain.service.user.UserDomainService;
import com.awsome.shop.auth.infrastructure.security.api.service.PasswordHashService;
import com.awsome.shop.auth.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 用户领域服务实现
 */
@Service
@RequiredArgsConstructor
public class UserDomainServiceImpl implements UserDomainService {

    private final UserRepository userRepository;
    private final PasswordHashService passwordHashService;

    @Override
    public UserEntity register(String username, String passwordHash, String displayName) {
        if (userRepository.existsByUsername(username)) {
            throw new BusinessException("CONFLICT_001", "用户名已存在: " + username);
        }
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPasswordHash(passwordHash);
        user.setDisplayName(displayName);
        user.setRole("employee");
        user.setStatus("active");
        return userRepository.save(user);
    }

    @Override
    public UserEntity authenticate(String username, String rawPassword) {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new BusinessException("AUTH_001", "用户名或密码错误");
        }
        if (!passwordHashService.verify(rawPassword, user.getPasswordHash())) {
            throw new BusinessException("AUTH_001", "用户名或密码错误");
        }
        return user;
    }

    @Override
    public UserEntity findById(Long id) {
        UserEntity user = userRepository.findById(id);
        if (user == null) {
            throw new BusinessException("NOT_FOUND_001", "用户不存在");
        }
        return user;
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void update(UserEntity user) {
        userRepository.update(user);
    }
}
