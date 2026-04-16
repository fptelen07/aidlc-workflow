package com.awsome.shop.point.application.api.dto.points.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 创建积分规则请求
 */
@Data
public class CreateRuleRequest {

    private Long operatorId;

    @NotBlank(message = "规则名称不能为空")
    private String name;

    @NotBlank(message = "规则类型不能为空")
    private String type;

    @NotNull(message = "积分数量不能为空")
    @Min(value = 1, message = "积分数量必须大于0")
    private Long amount;

    private Boolean enabled = true;

    private String description;
}
