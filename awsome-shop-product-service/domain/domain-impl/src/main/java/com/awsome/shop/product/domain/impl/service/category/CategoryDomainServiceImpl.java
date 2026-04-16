package com.awsome.shop.product.domain.impl.service.category;

import com.awsome.shop.product.common.exception.BusinessException;
import com.awsome.shop.product.domain.model.category.CategoryEntity;
import com.awsome.shop.product.domain.service.category.CategoryDomainService;
import com.awsome.shop.product.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryDomainServiceImpl implements CategoryDomainService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryEntity create(String name, Integer sortOrder) {
        CategoryEntity entity = new CategoryEntity();
        entity.setName(name);
        entity.setSortOrder(sortOrder != null ? sortOrder : 0);
        return categoryRepository.save(entity);
    }

    @Override
    public CategoryEntity update(Long id, String name, Integer sortOrder) {
        CategoryEntity entity = findById(id);
        entity.setName(name);
        entity.setSortOrder(sortOrder != null ? sortOrder : entity.getSortOrder());
        return categoryRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        int count = categoryRepository.countProductsByCategoryName(
                categoryRepository.findById(id).getName());
        if (count > 0) {
            throw new BusinessException("CONFLICT_001", "该分类下有商品，无法删除");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryEntity> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public CategoryEntity findById(Long id) {
        CategoryEntity entity = categoryRepository.findById(id);
        if (entity == null) {
            throw new BusinessException("NOT_FOUND_001", "分类不存在");
        }
        return entity;
    }
}
