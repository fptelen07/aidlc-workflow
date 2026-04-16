package com.awsome.shop.order.application.api.dto.order.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 确认订单请求
 */
@Data
public class ConfirmOrderRequest {

    @NotNull(message = "订单ID不能为空")
    private Long id;

    /**
     * 操作人ID（网关注入）
     */
    private Long operatorId;
}
