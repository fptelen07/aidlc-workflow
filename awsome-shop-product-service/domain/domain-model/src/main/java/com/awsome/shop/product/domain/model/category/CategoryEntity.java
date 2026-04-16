package com.awsome.shop.product.domain.model.category;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CategoryEntity {
    private Long id;
    private String name;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
