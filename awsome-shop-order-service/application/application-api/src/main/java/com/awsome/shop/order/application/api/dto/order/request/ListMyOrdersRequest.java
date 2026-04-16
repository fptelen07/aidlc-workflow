package com.awsome.shop.order.application.api.dto.order.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 查询我的订单列表请求
 */
@Data
public class ListMyOrdersRequest {

    /**
     * 操作人ID（网关注入）
     */
    private Long operatorId;

    /**
     * 订单状态筛选（可选）
     */
    private String status;

    @Min(value = 1, message = "页码最小为 1")
    private Integer page = 1;

    @Min(value = 1, message = "每页大小最小为 1")
    @Max(value = 100, message = "每页大小最大为 100")
    private Integer size = 20;
}
