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
    public void update(UserEntity user) {
        UserPO po = toPO(user);
        userMapper.updateById(po);
    }

    @Override
    public com.awsome.shop.auth.common.dto.PageResult<UserEntity> page(int page, int size, String keyword) {
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<UserPO> wrapper = new LambdaQueryWrapper<UserPO>();
        if (keyword != null && !keyword.isBlank()) {
            wrapper.like(UserPO::getUsername, keyword).or().like(UserPO::getDisplayName, keyword);
        }
        wrapper.orderByDesc(UserPO::getCreatedAt);
        com.baomidou.mybatisplus.core.metadata.IPage<UserPO> result = userMapper.selectPage(
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size), wrapper);
        com.awsome.shop.auth.common.dto.PageResult<UserEntity> pageResult = new com.awsome.shop.auth.common.dto.PageResult<>();
        pageResult.setCurrent(result.getCurrent());
        pageResult.setSize(result.getSize());
        pageResult.setTotal(result.getTotal());
        pageResult.setPages(result.getPages());
        pageResult.setRecords(result.getRecords().stream().map(this::toEntity).collect(java.util.stream.Collectors.toList()));
        return pageResult;
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
        e.setEmail(po.getEmail());
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
        po.setEmail(e.getEmail());
        po.setStatus(e.getStatus());
        return po;
    }
}
