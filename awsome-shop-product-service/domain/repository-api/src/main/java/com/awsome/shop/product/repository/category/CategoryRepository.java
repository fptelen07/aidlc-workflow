package com.awsome.shop.product.repository.category;

import com.awsome.shop.product.domain.model.category.CategoryEntity;
import java.util.List;

public interface CategoryRepository {
    CategoryEntity save(CategoryEntity entity);
    CategoryEntity update(CategoryEntity entity);
    CategoryEntity findById(Long id);
    List<CategoryEntity> findAll();
    void deleteById(Long id);
    int countProductsByCategoryName(String categoryName);
}
