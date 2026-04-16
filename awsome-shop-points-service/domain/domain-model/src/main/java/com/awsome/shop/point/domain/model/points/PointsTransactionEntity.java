package com.awsome.shop.point.domain.model.points;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 积分交易领域实体
 */
@Data
public class PointsTransactionEntity {

    private Long id;
    private Long userId;
    /** grant / deduct / expire */
    private String type;
    private Long amount;
    private String reason;
    private Long orderId;
    private LocalDateTime createdAt;
}
