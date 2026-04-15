# Unit 3: Product Service — Code Generation Summary

## 新建文件（18 个）
- CategoryEntity.java, CategoryDomainService.java, CategoryDomainServiceImpl.java, CategoryRepository.java
- CategoryPO.java, CategoryMapper.java, CategoryMapper.xml, CategoryRepositoryImpl.java
- CategoryApplicationService.java, CategoryApplicationServiceImpl.java, CategoryController.java
- CategoryDTO.java, CreateCategoryRequest.java, UpdateCategoryRequest.java, DeleteCategoryRequest.java
- UpdateProductRequest.java, DeleteProductRequest.java, GetProductRequest.java, ToggleStatusRequest.java, DeductStockRequest.java
- V3__create_category_table.sql, V4__seed_data.sql

## 修改文件（7 个）
- ProductDomainService.java — 添加 update, delete, updateStatus, deductStock
- ProductDomainServiceImpl.java — 实现新方法
- ProductRepository.java — 添加 updateStatus, deductStock
- ProductRepositoryImpl.java — 实现 updateStatus, deductStock
- ProductMapper.java — 添加 deductStock
- ProductMapper.xml — 添加 deductStock SQL
- ProductApplicationService.java + Impl — 添加 get, update, delete, toggleStatus, deductStock
- ProductController.java — 重写，7 个端点（原 2 个 + 新 5 个）

## 编译验证
- `mvn clean compile` 通过 ✅（EC2）
