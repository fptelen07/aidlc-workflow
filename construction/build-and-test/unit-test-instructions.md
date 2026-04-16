# Unit Test Instructions

## 测试策略
- 领域服务单元测试（Mockito mock 仓储和外部服务）
- 应用服务单元测试（Mockito mock 领域服务）
- 测试覆盖率目标：核心业务逻辑 > 70%

## 运行单元测试

### 后端（每个服务）
```bash
cd awsome-shop-auth-service && mvn test -q
cd ../awsome-shop-product-service && mvn test -q
cd ../awsome-shop-order-service && mvn test -q
cd ../awsome-shop-points-service && mvn test -q
cd ../awsome-shop-gateway-service && mvn test -q
```

### 前端
```bash
cd awsome-shop-frontend && npx vitest --run
```

## 关键测试场景

### Auth Service
- 注册：用户名唯一性校验、密码长度校验、BCrypt 加密
- 登录：正确凭据返回 token、错误凭据返回 AUTH_001
- Token：access token 2h 有效期、refresh token 7d、过期 token 验证失败

### Product Service
- 商品 CRUD：创建 SKU 唯一性、编辑、逻辑删除、上下架切换
- 分类：CRUD、有商品关联时禁止删除
- 库存扣减：原子操作、库存不足拒绝

### Points Service
- 积分发放：余额增加、交易记录
- 积分扣减：余额校验、余额不足拒绝
- 批量发放：多用户同时发放
- 统计：月度发放/扣减量

### Order Service
- 创建订单：状态为 pending
- 确认订单：pending → completed
- 拒绝订单：pending → rejected
- 状态校验：非 pending 状态不可操作

## 测试报告
- Maven: `target/surefire-reports/`
- Vitest: 控制台输出
