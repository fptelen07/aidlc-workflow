package com.awsome.shop.product.application.api.dto.category.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCategoryRequest {
    @NotNull(message = "分类ID不能为空")
    private Long id;
    private String name;
    private Integer sortOrder;
    private Long operatorId;
}
