package com.awsome.shop.point.application.api.dto.points.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 更新积分规则请求
 */
@Data
public class UpdateRuleRequest {

    private Long operatorId;

    @NotNull(message = "规则ID不能为空")
    private Long id;

    private String name;

    private String type;

    @Min(value = 1, message = "积分数量必须大于0")
    private Long amount;

    private Boolean enabled;

    private String description;
}
