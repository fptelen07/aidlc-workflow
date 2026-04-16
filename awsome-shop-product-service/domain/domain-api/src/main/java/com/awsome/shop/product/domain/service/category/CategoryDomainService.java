package com.awsome.shop.product.domain.service.category;

import com.awsome.shop.product.domain.model.category.CategoryEntity;
import java.util.List;

public interface CategoryDomainService {
    CategoryEntity create(String name, Integer sortOrder);
    CategoryEntity update(Long id, String name, Integer sortOrder);
    void delete(Long id);
    List<CategoryEntity> findAll();
    CategoryEntity findById(Long id);
}
