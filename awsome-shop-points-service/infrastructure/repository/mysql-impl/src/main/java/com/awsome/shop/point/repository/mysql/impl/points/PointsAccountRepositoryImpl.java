package com.awsome.shop.point.repository.mysql.impl.points;

import com.awsome.shop.point.domain.model.points.PointsAccountEntity;
import com.awsome.shop.point.repository.mysql.mapper.points.PointsAccountMapper;
import com.awsome.shop.point.repository.mysql.po.points.PointsAccountPO;
import com.awsome.shop.point.repository.points.PointsAccountRepository;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 积分账户仓储实现
 */
@Repository
@RequiredArgsConstructor
public class PointsAccountRepositoryImpl implements PointsAccountRepository {

    private final PointsAccountMapper accountMapper;

    @Override
    public PointsAccountEntity findByUserId(Long userId) {
        LambdaQueryWrapper<PointsAccountPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PointsAccountPO::getUserId, userId);
        PointsAccountPO po = accountMapper.selectOne(wrapper);
        return po == null ? null : toEntity(po);
    }

    @Override
    public PointsAccountEntity save(PointsAccountEntity account) {
        PointsAccountPO po = toPO(account);
        accountMapper.insert(po);
        account.setId(po.getId());
        return account;
    }

    @Override
    public void updateBalance(Long userId, Long amount) {
        accountMapper.updateBalance(userId, amount);
    }

    private PointsAccountEntity toEntity(PointsAccountPO po) {
        PointsAccountEntity entity = new PointsAccountEntity();
        entity.setId(po.getId());
        entity.setUserId(po.getUserId());
        entity.setBalance(po.getBalance());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }

    private PointsAccountPO toPO(PointsAccountEntity entity) {
        PointsAccountPO po = new PointsAccountPO();
        po.setId(entity.getId());
        po.setUserId(entity.getUserId());
        po.setBalance(entity.getBalance());
        return po;
    }
}
