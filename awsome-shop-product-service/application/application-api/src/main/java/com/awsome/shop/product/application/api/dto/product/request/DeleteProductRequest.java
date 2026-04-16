package com.awsome.shop.product.application.api.dto.product.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteProductRequest {
    @NotNull(message = "商品ID不能为空")
    private Long id;
    private Long operatorId;
}
