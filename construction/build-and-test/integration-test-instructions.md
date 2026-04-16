# Integration Test Instructions

## 测试策略
- 仓储层集成测试（TestContainers + MySQL + Redis）
- 跨服务集成测试（需要所有服务运行）
- API 端到端测试（通过 Gateway）

## 前置条件
- Docker（用于 TestContainers）
- 所有 5 个后端服务运行中
- MySQL + Redis 运行中

## 仓储层集成测试

### 运行（每个服务）
```bash
cd awsome-shop-auth-service && mvn verify -P integration-test
cd ../awsome-shop-product-service && mvn verify -P integration-test
cd ../awsome-shop-order-service && mvn verify -P integration-test
cd ../awsome-shop-points-service && mvn verify -P integration-test
```

## 跨服务集成测试场景

### 场景 1: 用户注册 → 登录 → 获取信息
1. POST /api/v1/public/auth/register — 注册新用户
2. POST /api/v1/public/auth/login — 登录获取 token
3. POST /api/v1/auth/me — 使用 token 获取用户信息
4. 验证：返回正确的用户信息

### 场景 2: 商品浏览 → 兑换 → 订单管理
1. POST /api/v1/public/product/list — 获取商品列表
2. POST /api/v1/order/create — 创建兑换订单
3. POST /api/v1/order/my-list — 查看我的订单（状态 pending）
4. POST /api/v1/order/confirm — 管理员确认订单
5. 验证：订单状态变为 completed

### 场景 3: 积分发放 → 查询 → 扣减
1. POST /api/v1/point/grant — 管理员发放积分
2. POST /api/v1/point/balance — 查询余额（应增加）
3. POST /api/v1/point/deduct — 扣减积分
4. POST /api/v1/point/my-history — 查看变动历史
5. 验证：余额正确、历史记录完整

### 场景 4: Gateway Token 验证
1. 不带 token 访问 protected 端点 → 401
2. 带有效 token 访问 → 200
3. 带过期 token 访问 → 401

## 手动测试脚本
```bash
# 登录获取 token
TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/public/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"admin","password":"admin123"}' | jq -r '.data.accessToken')

# 使用 token 访问 protected 端点
curl -X POST http://localhost:8080/api/v1/point/balance \
  -H 'Content-Type: application/json' \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"operatorId": 1}'
```
