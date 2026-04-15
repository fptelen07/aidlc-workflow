# 应用设计总览

## 设计概要

AWSome Shop 采用 DDD 六边形架构，5 个微服务 + 1 个前端 SPA。本文档汇总应用设计的关键决策。

## 组件总览

| 服务 | 领域模型 | 领域服务 | 应用服务 | API 端点 |
|------|----------|----------|----------|----------|
| Auth | User, Role | UserDomainService, PasswordService | AuthApplicationService | 5 个 |
| Product | Product, Category, ProductStatus | ProductDomainService, CategoryDomainService | ProductApplicationService, CategoryApplicationService | 11 个 |
| Order | Order, OrderStatus | OrderDomainService | OrderApplicationService | 6 个 |
| Points | PointsAccount, PointsTransaction, PointsRule, TransactionType | PointsDomainService, PointsRuleDomainService, PointsExpirationService | PointsApplicationService, PointsRuleApplicationService | 11 个 |
| Frontend | — | — | 7 个 API Service | 9 个新页面 |

**总计**: 33 个 API 端点，9 个新前端页面，7 个 API Service 模块

## 关键设计决策

1. **POST-only API** — 所有端点使用 POST，动作写在 URL 路径中
2. **Gateway 注入 operatorId** — 认证请求由 Gateway 注入 operatorId 到请求体
3. **同步 HTTP 跨服务调用** — Order Service 通过 Gateway 调用 Product/Points Service
4. **订单确认补偿机制** — 积分扣减成功但库存扣减失败时，需回滚积分

## 详细设计文档

- [components.md](components.md) — 组件定义与职责
- [component-methods.md](component-methods.md) — 方法签名
- [services.md](services.md) — 服务定义与编排模式
- [component-dependency.md](component-dependency.md) — 依赖关系与数据流
