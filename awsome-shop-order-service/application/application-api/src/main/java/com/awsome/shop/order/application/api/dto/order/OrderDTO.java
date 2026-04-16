package com.awsome.shop.order.application.api.dto.order;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Order 数据传输对象
 */
@Data
public class OrderDTO {

    private Long id;

    private Long userId;

    private String userName;

    private Long productId;

    private String productName;

    private Integer pointsAmount;

    private String status;

    private String redemptionCode;

    private LocalDateTime createdAt;
}
