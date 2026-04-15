# Code Generation Plan — Unit 3: Product Service

## Unit Context
- **Unit**: Product Service (商品服务)
- **Stories**: US-010 (商品与分类管理), US-011 (商品浏览与详情)
- **Dependencies**: Unit 1 (Tech Debt) ✅
- **Scope**: 大量已有代码需扩展 + 新增 Category 全栈

## 已有代码
- ProductEntity, ProductDomainService/Impl, ProductRepository/Impl, ProductPO, ProductMapper
- ProductApplicationService/Impl (list + create only)
- ProductController (create + list only)
- ProductDTO, CreateProductRequest, ListProductRequest
- V2__create_product_table.sql

## 需要新增/修改

---

## Execution Steps

### Step 1: Category 领域模型
- [ ] 1.1 创建 CategoryEntity.java
- [ ] 1.2 创建 CategoryDomainService.java (接口)
- [ ] 1.3 创建 CategoryDomainServiceImpl.java
- [ ] 1.4 创建 CategoryRepository.java (接口)

### Step 2: Product 领域服务扩展
- [ ] 2.1 修改 ProductDomainService.java — 添加 update, delete, updateStatus, deductStock 方法
- [ ] 2.2 修改 ProductDomainServiceImpl.java — 实现新方法
- [ ] 2.3 修改 ProductRepository.java — 添加 updateStatus, deductStock 方法

### Step 3: Category 基础设施
- [ ] 3.1 创建 CategoryPO.java
- [ ] 3.2 创建 CategoryMapper.java
- [ ] 3.3 创建 CategoryMapper.xml
- [ ] 3.4 创建 CategoryRepositoryImpl.java

### Step 4: Product 基础设施扩展
- [ ] 4.1 修改 ProductRepositoryImpl.java — 实现 updateStatus, deductStock
- [ ] 4.2 修改 ProductMapper.xml — 添加 deductStock SQL

### Step 5: 应用层 DTO（新增）
- [ ] 5.1 创建 UpdateProductRequest.java
- [ ] 5.2 创建 DeleteProductRequest.java
- [ ] 5.3 创建 GetProductRequest.java
- [ ] 5.4 创建 ToggleStatusRequest.java
- [ ] 5.5 创建 DeductStockRequest.java
- [ ] 5.6 创建 CategoryDTO.java
- [ ] 5.7 创建 CreateCategoryRequest.java
- [ ] 5.8 创建 UpdateCategoryRequest.java
- [ ] 5.9 创建 DeleteCategoryRequest.java

### Step 6: 应用服务扩展
- [ ] 6.1 修改 ProductApplicationService.java — 添加 get, update, delete, toggleStatus, deductStock
- [ ] 6.2 修改 ProductApplicationServiceImpl.java — 实现新方法
- [ ] 6.3 创建 CategoryApplicationService.java (接口)
- [ ] 6.4 创建 CategoryApplicationServiceImpl.java

### Step 7: Controller 扩展
- [ ] 7.1 修改 ProductController.java — 添加 get, update, delete, toggle-status, deduct-stock 端点；修复 create 路径
- [ ] 7.2 创建 CategoryController.java — 4 个端点

### Step 8: Flyway 迁移
- [ ] 8.1 创建 V3__create_category_table.sql
- [ ] 8.2 创建 V4__seed_data.sql — 默认分类 + 示例商品

### Step 9: 验证编译（EC2）
- [ ] 9.1 上传并编译

### Step 10: 文档
- [ ] 10.1 创建 code-summary.md
