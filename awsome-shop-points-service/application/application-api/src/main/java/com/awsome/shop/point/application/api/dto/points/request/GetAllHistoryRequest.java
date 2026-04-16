package com.awsome.shop.point.application.api.dto.points.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 查询所有积分历史请求（管理员）
 */
@Data
public class GetAllHistoryRequest {

    private Long operatorId;

    private String type;

    @Min(value = 1, message = "页码最小为 1")
    private Integer page = 1;

    @Min(value = 1, message = "每页大小最小为 1")
    @Max(value = 100, message = "每页大小最大为 100")
    private Integer size = 20;
}
