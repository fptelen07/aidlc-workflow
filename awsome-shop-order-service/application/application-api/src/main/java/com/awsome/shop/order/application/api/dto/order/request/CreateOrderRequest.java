package com.awsome.shop.order.application.api.dto.order.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 创建订单请求
 */
@Data
public class CreateOrderRequest {

    @NotNull(message = "商品ID不能为空")
    private Long productId;

    /**
     * 操作人ID（网关注入）
     */
    private Long operatorId;
}
