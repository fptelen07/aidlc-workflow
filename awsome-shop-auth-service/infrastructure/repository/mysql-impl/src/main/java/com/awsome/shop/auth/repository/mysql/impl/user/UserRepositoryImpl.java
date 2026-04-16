package com.awsome.shop.auth.repository.mysql.impl.user;

import com.awsome.shop.auth.domain.model.user.UserEntity;
import com.awsome.shop.auth.repository.mysql.mapper.user.UserMapper;
import com.awsome.shop.auth.repository.mysql.po.user.UserPO;
import com.awsome.shop.auth.repository.user.UserRepository;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper userMapper;

    @Override
    public UserEntity save(UserEntity user) {
        UserPO po = toPO(user);
        userMapper.insert(po);
        user.setId(po.getId());
        return user;
    }

    @Override
    public UserEntity findById(Long id) {
        UserPO po = userMapper.selectById(id);
        return po == null ? null : toEntity(po);
    }

    @Override
    public UserEntity findByUsername(String username) {
        UserPO po = userMapper.selectOne(
                new LambdaQueryWrapper<UserPO>().eq(UserPO::getUsername, username));
        return po == null ? null : toEntity(po);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userMapper.exists(
                new LambdaQueryWrapper<UserPO>().eq(UserPO::getUsername, username));
    }

    private UserEntity toEntity(UserPO po) {
        UserEntity e = new UserEntity();
        e.setId(po.getId());
        e.setUsername(po.getUsername());
        e.setPasswordHash(po.getPasswordHash());
        e.setDisplayName(po.getDisplayName());
        e.setRole(po.getRole());
        e.setAvatar(po.getAvatar());
        e.setStatus(po.getStatus());
        e.setCreatedAt(po.getCreatedAt());
        e.setUpdatedAt(po.getUpdatedAt());
        return e;
    }

    private UserPO toPO(UserEntity e) {
        UserPO po = new UserPO();
        po.setId(e.getId());
        po.setUsername(e.getUsername());
        po.setPasswordHash(e.getPasswordHash());
        po.setDisplayName(e.getDisplayName());
        po.setRole(e.getRole());
        po.setAvatar(e.getAvatar());
        po.setStatus(e.getStatus());
        return po;
    }
}
