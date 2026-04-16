package com.awsome.shop.point.application.api.dto.points.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 批量发放积分请求
 */
@Data
public class BatchGrantRequest {

    private Long operatorId;

    @NotEmpty(message = "用户ID列表不能为空")
    private List<Long> userIds;

    @NotNull(message = "积分数量不能为空")
    @Min(value = 1, message = "积分数量必须大于0")
    private Long amount;

    private String reason;
}
