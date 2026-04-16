package com.awsome.shop.order.domain.impl.service.order;

import com.awsome.shop.order.common.dto.PageResult;
import com.awsome.shop.order.common.enums.OrderErrorCode;
import com.awsome.shop.order.common.exception.BusinessException;
import com.awsome.shop.order.domain.model.order.OrderEntity;
import com.awsome.shop.order.domain.service.order.OrderDomainService;
import com.awsome.shop.order.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Order 领域服务实现
 */
@Service
@RequiredArgsConstructor
public class OrderDomainServiceImpl implements OrderDomainService {

    private final OrderRepository orderRepository;

    @Override
    public OrderEntity create(OrderEntity order) {
        order.setStatus("pending");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
        return orderRepository.findById(order.getId());
    }

    @Override
    public OrderEntity confirm(Long id) {
        OrderEntity order = findById(id);
        if (!"pending".equals(order.getStatus())) {
            throw new BusinessException(OrderErrorCode.INVALID_STATUS);
        }
        order.setStatus("completed");
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.update(order);
        return orderRepository.findById(id);
    }

    @Override
    public OrderEntity reject(Long id) {
        OrderEntity order = findById(id);
        if (!"pending".equals(order.getStatus())) {
            throw new BusinessException(OrderErrorCode.INVALID_STATUS);
        }
        order.setStatus("rejected");
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.update(order);
        return orderRepository.findById(id);
    }

    @Override
    public void update(OrderEntity order) {
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.update(order);
    }

    @Override
    public OrderEntity findById(Long id) {
        OrderEntity entity = orderRepository.findById(id);
        if (entity == null) {
            throw new BusinessException(OrderErrorCode.ORDER_NOT_FOUND);
        }
        return entity;
    }

    @Override
    public PageResult<OrderEntity> listByUser(Long userId, String status, int page, int size) {
        return orderRepository.pageByUserId(userId, status, page, size);
    }

    @Override
    public PageResult<OrderEntity> listAll(String status, String userName, int page, int size) {
        return orderRepository.pageAll(status, userName, page, size);
    }
}
