package com.awsome.shop.product.application.api.dto.product.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class UpdateProductRequest {
    @NotNull(message = "商品ID不能为空")
    private Long id;
    private String name;
    private String sku;
    private String category;
    private String brand;
    private Integer pointsPrice;
    private BigDecimal marketPrice;
    private Integer stock;
    private Integer status;
    private String description;
    private String imageUrl;
    private String subtitle;
    private String deliveryMethod;
    private String serviceGuarantee;
    private String promotion;
    private String colors;
    private List<Map<String, String>> specs;
    private Long operatorId;
}
