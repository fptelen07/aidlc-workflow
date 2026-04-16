package com.awsome.shop.product.repository.mysql.impl.category;

import com.awsome.shop.product.domain.model.category.CategoryEntity;
import com.awsome.shop.product.repository.category.CategoryRepository;
import com.awsome.shop.product.repository.mysql.mapper.category.CategoryMapper;
import com.awsome.shop.product.repository.mysql.mapper.product.ProductMapper;
import com.awsome.shop.product.repository.mysql.po.category.CategoryPO;
import com.awsome.shop.product.repository.mysql.po.product.ProductPO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;

    @Override
    public CategoryEntity save(CategoryEntity entity) {
        CategoryPO po = toPO(entity);
        categoryMapper.insert(po);
        entity.setId(po.getId());
        return entity;
    }

    @Override
    public CategoryEntity update(CategoryEntity entity) {
        CategoryPO po = toPO(entity);
        categoryMapper.updateById(po);
        return findById(entity.getId());
    }

    @Override
    public CategoryEntity findById(Long id) {
        CategoryPO po = categoryMapper.selectById(id);
        return po == null ? null : toEntity(po);
    }

    @Override
    public List<CategoryEntity> findAll() {
        List<CategoryPO> list = categoryMapper.selectList(
                new LambdaQueryWrapper<CategoryPO>().orderByAsc(CategoryPO::getSortOrder));
        return list.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        categoryMapper.deleteById(id);
    }

    @Override
    public int countProductsByCategoryName(String categoryName) {
        return Math.toIntExact(productMapper.selectCount(
                new LambdaQueryWrapper<ProductPO>().eq(ProductPO::getCategory, categoryName)));
    }

    private CategoryEntity toEntity(CategoryPO po) {
        CategoryEntity e = new CategoryEntity();
        e.setId(po.getId());
        e.setName(po.getName());
        e.setSortOrder(po.getSortOrder());
        e.setCreatedAt(po.getCreatedAt());
        e.setUpdatedAt(po.getUpdatedAt());
        return e;
    }

    private CategoryPO toPO(CategoryEntity e) {
        CategoryPO po = new CategoryPO();
        po.setId(e.getId());
        po.setName(e.getName());
        po.setSortOrder(e.getSortOrder());
        return po;
    }
}
