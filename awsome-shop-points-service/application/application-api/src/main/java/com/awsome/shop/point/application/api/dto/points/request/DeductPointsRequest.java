package com.awsome.shop.point.application.api.dto.points.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 扣减积分请求
 */
@Data
public class DeductPointsRequest {

    private Long operatorId;

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotNull(message = "积分数量不能为空")
    @Min(value = 1, message = "积分数量必须大于0")
    private Long amount;

    private String reason;

    private Long orderId;
}
