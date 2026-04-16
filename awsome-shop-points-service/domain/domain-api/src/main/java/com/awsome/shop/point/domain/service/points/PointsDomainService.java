package com.awsome.shop.point.domain.service.points;

import com.awsome.shop.point.common.dto.PageResult;
import com.awsome.shop.point.domain.model.points.PointsTransactionEntity;

import java.util.List;

/**
 * 积分领域服务接口
 */
public interface PointsDomainService {

    Long getBalance(Long userId);

    void grant(Long userId, Long amount, String reason);

    void deduct(Long userId, Long amount, String reason, Long orderId);

    void batchGrant(List<Long> userIds, Long amount, String reason);

    PageResult<PointsTransactionEntity> getHistory(Long userId, String type, int page, int size);

    PageResult<PointsTransactionEntity> getAllHistory(String type, int page, int size);

    Long sumGrantByMonth(int year, int month);

    Long sumDeductByMonth(int year, int month);
}
