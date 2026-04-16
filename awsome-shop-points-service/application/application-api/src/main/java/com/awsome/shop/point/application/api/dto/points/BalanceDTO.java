package com.awsome.shop.point.application.api.dto.points;

import lombok.Data;

/**
 * 积分余额 DTO
 */
@Data
public class BalanceDTO {

    private Long userId;
    private Long balance;
}
