package com.awsome.shop.product.repository.mysql.mapper.category;

import com.awsome.shop.product.repository.mysql.po.category.CategoryPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<CategoryPO> {
}
