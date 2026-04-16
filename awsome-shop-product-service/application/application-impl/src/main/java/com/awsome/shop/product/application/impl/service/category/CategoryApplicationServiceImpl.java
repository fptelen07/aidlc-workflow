package com.awsome.shop.product.application.impl.service.category;

import com.awsome.shop.product.application.api.dto.category.CategoryDTO;
import com.awsome.shop.product.application.api.dto.category.request.*;
import com.awsome.shop.product.application.api.service.category.CategoryApplicationService;
import com.awsome.shop.product.domain.model.category.CategoryEntity;
import com.awsome.shop.product.domain.service.category.CategoryDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryApplicationServiceImpl implements CategoryApplicationService {

    private final CategoryDomainService categoryDomainService;

    @Override
    public CategoryDTO create(CreateCategoryRequest request) {
        return toDTO(categoryDomainService.create(request.getName(), request.getSortOrder()));
    }

    @Override
    public CategoryDTO update(UpdateCategoryRequest request) {
        return toDTO(categoryDomainService.update(request.getId(), request.getName(), request.getSortOrder()));
    }

    @Override
    public void delete(DeleteCategoryRequest request) {
        categoryDomainService.delete(request.getId());
    }

    @Override
    public List<CategoryDTO> listAll() {
        return categoryDomainService.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    private CategoryDTO toDTO(CategoryEntity entity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSortOrder(entity.getSortOrder());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
}
