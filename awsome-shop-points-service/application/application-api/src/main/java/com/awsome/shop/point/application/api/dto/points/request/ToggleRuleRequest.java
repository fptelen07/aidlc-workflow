package com.awsome.shop.point.application.api.dto.points.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 切换积分规则启用状态请求
 */
@Data
public class ToggleRuleRequest {

    private Long operatorId;

    @NotNull(message = "规则ID不能为空")
    private Long id;
}
