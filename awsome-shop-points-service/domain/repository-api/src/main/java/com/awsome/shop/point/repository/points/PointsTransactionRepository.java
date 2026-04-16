package com.awsome.shop.point.repository.points;

import com.awsome.shop.point.common.dto.PageResult;
import com.awsome.shop.point.domain.model.points.PointsTransactionEntity;

/**
 * 积分交易仓储接口
 */
public interface PointsTransactionRepository {

    PointsTransactionEntity save(PointsTransactionEntity tx);

    PageResult<PointsTransactionEntity> pageByUserId(Long userId, String type, int page, int size);

    PageResult<PointsTransactionEntity> pageAll(String type, int page, int size);

    /**
     * 统计指定月份的发放总量
     */
    Long sumGrantByMonth(int year, int month);

    /**
     * 统计指定月份的扣减总量
     */
    Long sumDeductByMonth(int year, int month);
}
