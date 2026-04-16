package com.awsome.shop.order.domain.model.order;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Order 领域实体
 */
@Data
public class OrderEntity {

    private Long id;

    private Long userId;

    private String userName;

    private Long productId;

    private String productName;

    private Integer pointsAmount;

    /**
     * 订单状态：pending / completed / rejected
     */
    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
