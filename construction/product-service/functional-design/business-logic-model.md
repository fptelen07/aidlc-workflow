# Unit 3: Product Service — 功能设计

## 业务逻辑模型

Product Service 负责商品 CRUD、分类 CRUD、商品浏览/详情、上下架、库存扣减。

---

## US-010: 商品与分类管理（后端）

### 商品管理
- 创建商品：名称/SKU/分类/积分价格/库存/图片/状态，SKU 唯一性校验
- 编辑商品：修改所有字段
- 删除商品：逻辑删除（MyBatis-Plus @TableLogic）
- 商品列表：分页 + 按名称搜索 + 按分类筛选 + 按状态筛选
- 上架/下架切换：status 0↔1

### 分类管理
- 创建分类：名称 + 排序权重
- 编辑分类：修改名称和排序
- 删除分类：有商品关联时禁止删除
- 分类列表：全量查询

### 库存扣减（Order Service 调用）
- 原子扣减：`UPDATE product SET stock = stock - #{quantity} WHERE id = #{id} AND stock >= #{quantity}`
- 库存不足时抛出异常

---

## US-011: 商品浏览与详情（后端）

### 商品列表（员工端）
- 仅返回 status=1（上架）且 stock > 0 的商品
- 分页 + 按分类筛选 + 按名称搜索

### 商品详情
- 返回完整商品信息

---

## 领域模型

### CategoryEntity（新增）
```
CategoryEntity:
  id: Long (PK, auto-increment)
  name: String (not null)
  sortOrder: Integer (default 0)
  createdAt: LocalDateTime
  updatedAt: LocalDateTime
```

### ProductEntity（已存在，无需修改）

---

## 新增 API 端点

| 端点 | 方法 | 认证 | 描述 |
|------|------|------|------|
| /api/v1/public/product/get | POST | 无 | 商品详情 |
| /api/v1/product/update | POST | 需认证 | 编辑商品 |
| /api/v1/product/delete | POST | 需认证 | 删除商品 |
| /api/v1/product/toggle-status | POST | 需认证 | 上架/下架 |
| /api/v1/product/deduct-stock | POST | 需认证 | 库存扣减 |
| /api/v1/public/category/list | POST | 无 | 分类列表 |
| /api/v1/category/create | POST | 需认证 | 创建分类 |
| /api/v1/category/update | POST | 需认证 | 编辑分类 |
| /api/v1/category/delete | POST | 需认证 | 删除分类 |

### 已有端点（需修改路径）
- /api/v1/product/create → 改为 protected（去掉 public）
- /api/v1/public/product/list → 保持 public

---

## Flyway 迁移

### V3__create_category_table.sql
```sql
CREATE TABLE `category` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `sort_order` INT NOT NULL DEFAULT 0,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);
```

### V4__seed_data.sql
- 5 个默认分类：数码电子、生活家居、美食餐饮、礼品卡券、办公用品
- 示例商品数据
