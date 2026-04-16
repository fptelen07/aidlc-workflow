package com.awsome.shop.order.repository.order;

import com.awsome.shop.order.common.dto.PageResult;
import com.awsome.shop.order.domain.model.order.OrderEntity;

/**
 * Order 仓储接口
 */
public interface OrderRepository {

    void save(OrderEntity entity);

    void update(OrderEntity entity);

    OrderEntity findById(Long id);

    PageResult<OrderEntity> pageByUserId(Long userId, String status, int page, int size);

    PageResult<OrderEntity> pageAll(String status, String userName, int page, int size);
}
