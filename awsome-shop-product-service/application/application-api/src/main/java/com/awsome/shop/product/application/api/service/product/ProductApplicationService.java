package com.awsome.shop.product.application.api.service.product;

import com.awsome.shop.product.application.api.dto.product.ProductDTO;
import com.awsome.shop.product.application.api.dto.product.request.CreateProductRequest;
import com.awsome.shop.product.application.api.dto.product.request.DeductStockRequest;
import com.awsome.shop.product.application.api.dto.product.request.DeleteProductRequest;
import com.awsome.shop.product.application.api.dto.product.request.GetProductRequest;
import com.awsome.shop.product.application.api.dto.product.request.ListProductRequest;
import com.awsome.shop.product.application.api.dto.product.request.ToggleStatusRequest;
import com.awsome.shop.product.application.api.dto.product.request.UpdateProductRequest;
import com.awsome.shop.product.common.dto.PageResult;

/**
 * Product 应用服务接口
 */
public interface ProductApplicationService {

    PageResult<ProductDTO> list(ListProductRequest request);

    ProductDTO get(GetProductRequest request);

    ProductDTO create(CreateProductRequest request);

    ProductDTO update(UpdateProductRequest request);

    void delete(DeleteProductRequest request);

    void toggleStatus(ToggleStatusRequest request);

    void deductStock(DeductStockRequest request);
}
