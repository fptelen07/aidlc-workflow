package com.awsome.shop.point.application.api.dto.points;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 积分交易 DTO
 */
@Data
public class TransactionDTO {

    private Long id;
    private Long userId;
    private String type;
    private Long amount;
    private String reason;
    private Long orderId;
    private LocalDateTime createdAt;
}
