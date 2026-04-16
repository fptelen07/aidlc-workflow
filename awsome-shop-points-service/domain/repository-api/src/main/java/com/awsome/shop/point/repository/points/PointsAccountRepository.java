package com.awsome.shop.point.repository.points;

import com.awsome.shop.point.domain.model.points.PointsAccountEntity;

/**
 * 积分账户仓储接口
 */
public interface PointsAccountRepository {

    PointsAccountEntity findByUserId(Long userId);

    PointsAccountEntity save(PointsAccountEntity account);

    void updateBalance(Long userId, Long amount);
}
