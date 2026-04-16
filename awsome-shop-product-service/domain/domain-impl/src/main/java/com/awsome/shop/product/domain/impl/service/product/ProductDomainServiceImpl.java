package com.awsome.shop.product.domain.impl.service.product;

import com.awsome.shop.product.common.dto.PageResult;
import com.awsome.shop.product.common.enums.ProductErrorCode;
import com.awsome.shop.product.common.exception.BusinessException;
import com.awsome.shop.product.domain.model.product.ProductEntity;
import com.awsome.shop.product.domain.service.product.ProductDomainService;
import com.awsome.shop.product.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Product 领域服务实现
 */
@Service
@RequiredArgsConstructor
public class ProductDomainServiceImpl implements ProductDomainService {

    private final ProductRepository productRepository;

    @Override
    public ProductEntity getById(Long id) {
        ProductEntity entity = productRepository.getById(id);
        if (entity == null) {
            throw new BusinessException(ProductErrorCode.RESOURCE_NOT_FOUND);
        }
        return entity;
    }

    @Override
    public PageResult<ProductEntity> page(int page, int size, String name, String category) {
        return productRepository.page(page, size, name, category);
    }

    @Override
    public ProductEntity create(String name, String sku, String category, String brand,
                                Integer pointsPrice, BigDecimal marketPrice, Integer stock,
                                Integer status, String description, String imageUrl,
                                String subtitle, String deliveryMethod, String serviceGuarantee,
                                String promotion, String colors, List<Map<String, String>> specs) {
        // SKU 唯一性校验
        ProductEntity existing = productRepository.getBySku(sku);
        if (existing != null) {
            throw new BusinessException(ProductErrorCode.RESOURCE_ALREADY_EXISTS, sku);
        }

        ProductEntity entity = new ProductEntity();
        entity.setName(name);
        entity.setSku(sku);
        entity.setCategory(category);
        entity.setBrand(brand);
        entity.setPointsPrice(pointsPrice);
        entity.setMarketPrice(marketPrice);
        entity.setStock(stock != null ? stock : 0);
        entity.setStatus(status != null ? status : 0);
        entity.setDescription(description);
        entity.setImageUrl(imageUrl);
        entity.setSubtitle(subtitle);
        entity.setDeliveryMethod(deliveryMethod);
        entity.setServiceGuarantee(serviceGuarantee);
        entity.setPromotion(promotion);
        entity.setColors(colors);
        entity.setSpecs(specs);

        productRepository.save(entity);
        return productRepository.getById(entity.getId());
    }

    @Override
    public void update(ProductEntity entity) {
        getById(entity.getId()); // verify exists
        productRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        getById(id); // verify exists
        productRepository.deleteById(id);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        getById(id); // verify exists
        productRepository.updateStatus(id, status);
    }

    @Override
    public void deductStock(Long id, int quantity) {
        ProductEntity product = getById(id);
        if (product.getStock() < quantity) {
            throw new BusinessException(ProductErrorCode.RESOURCE_NOT_FOUND.getCode(), "库存不足");
        }
        productRepository.deductStock(id, quantity);
    }
}
