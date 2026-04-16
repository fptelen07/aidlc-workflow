package com.awsome.shop.order.domain.service.order;

import com.awsome.shop.order.common.dto.PageResult;
import com.awsome.shop.order.domain.model.order.OrderEntity;

/**
 * Order 领域服务接口
 */
public interface OrderDomainService {

    OrderEntity create(OrderEntity order);

    OrderEntity confirm(Long id);

    OrderEntity reject(Long id);

    void update(OrderEntity order);

    OrderEntity findById(Long id);

    PageResult<OrderEntity> listByUser(Long userId, String status, int page, int size);

    PageResult<OrderEntity> listAll(String status, String userName, int page, int size);
}
