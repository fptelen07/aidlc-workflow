package com.awsome.shop.product.application.api.dto.category;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private Integer sortOrder;
    private LocalDateTime createdAt;
}
