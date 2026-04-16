# Build and Test Summary

## 部署环境
- **EC2**: ec2-54-89-171-32.compute-1.amazonaws.com
- **Java**: OpenJDK 21.0.10
- **Maven**: 3.8.4
- **Node.js**: 22.22.2
- **MySQL**: MariaDB 10.5.29
- **Redis**: Docker redis:7-alpine

## 构建状态

| 服务 | 编译 | 打包 | 启动 | 端口 |
|------|------|------|------|------|
| Auth Service | ✅ | ✅ | ✅ | 8001 |
| Product Service | ✅ | ✅ | ✅ | 8002 |
| Points Service | ✅ | ✅ | ✅ | 8003 |
| Order Service | ✅ | ✅ | ✅ | 8004 |
| Gateway Service | ✅ | ✅ | ✅ | 8080 |
| Frontend | ✅ (tsc) | — | — | — |

## API 验证结果

| 测试 | 方式 | 结果 |
|------|------|------|
| 用户注册 | 直接 8001 | ✅ code:0 |
| Admin 登录 (admin/admin123) | 直接 8001 | ✅ code:0, JWT 返回 |
| Employee 登录 (employee/emp123) | 直接 8001 | ✅ code:0, JWT 返回 |
| 商品列表 (5 个商品) | Gateway 8080 | ✅ code:0, total:5 |
| 分类列表 (5 个分类) | Gateway 8080 | ✅ code:0, count:5 |
| 积分余额 (employee=2580) | 直接 8003 | ✅ code:0, balance:2580 |
| 创建订单 | 直接 8004 | ✅ code:0, status:pending |

## Flyway 迁移

| 服务 | 迁移 | 状态 |
|------|------|------|
| Auth | V2 user 表 + V3 种子数据 | ✅ |
| Product | V2 product 表 + V3 category 表 + V4 种子数据 | ✅ |
| Points | V2 三张表 + V3 种子数据 | ✅ |
| Order | V2 order 表 | ✅ |

## Gateway 路由修复
- 添加 category-public 路由 (/api/v1/public/category/**)
- 添加 category-protected 路由 (/api/v1/category/**)
- 添加 auth-internal 路由 (/api/v1/internal/auth/**)

## 种子数据修复
- admin/admin123 和 employee/emp123 的 BCrypt hash 通过注册 API 重新生成

## 待完成项
- 单元测试编写（当前无测试代码）
- 集成测试（跨服务调用）
- Order Service 的跨服务调用实现（TODO 标记）
- 前端部署（需要配置 API 代理）
- 性能测试

## 启动命令
```bash
ssh -i "my-key.pem" ec2-user@ec2-54-89-171-32.compute-1.amazonaws.com
bash ~/start-all.sh
```
