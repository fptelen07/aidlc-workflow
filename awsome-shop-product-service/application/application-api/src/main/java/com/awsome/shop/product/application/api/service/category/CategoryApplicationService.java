package com.awsome.shop.product.application.api.service.category;

import com.awsome.shop.product.application.api.dto.category.CategoryDTO;
import com.awsome.shop.product.application.api.dto.category.request.*;
import java.util.List;

public interface CategoryApplicationService {
    CategoryDTO create(CreateCategoryRequest request);
    CategoryDTO update(UpdateCategoryRequest request);
    void delete(DeleteCategoryRequest request);
    List<CategoryDTO> listAll();
}
