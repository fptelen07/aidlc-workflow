# Code Generation Plan — Unit 1: Tech Debt

## Unit Context
- **Unit**: Tech Debt (技术债务修复)
- **Stories**: US-001, US-002, US-003, US-004
- **Dependencies**: None (first unit)
- **Scope**: 5 backend services + 1 frontend, pure refactoring

---

## Execution Steps

### Step 1: US-001 — 统一 Result 类（5 个后端服务）
- [x] 1.1 删除 `awsome-shop-auth-service/common/src/main/java/com/awsome/shop/auth/common/result/Result.java` — N/A（文件不存在）
- [x] 1.2 删除 `awsome-shop-product-service/common/src/main/java/com/awsome/shop/product/common/result/Result.java` — N/A（文件不存在）
- [x] 1.3 删除 `awsome-shop-order-service/common/src/main/java/com/awsome/shop/order/common/result/Result.java` — N/A（文件不存在）
- [x] 1.4 删除 `awsome-shop-points-service/common/src/main/java/com/awsome/shop/point/common/result/Result.java` — N/A（文件不存在）
- [x] 1.5 删除 `awsome-shop-gateway-service/common/src/main/java/com/awsome/shop/gateway/common/result/Result.java` — N/A（文件不存在）
- [x] 1.6 更新所有引用 `common.result.Result` 的文件 — 仅 TestController 引用，已在 Step 4 中随 TestController 一起删除

### Step 2: US-002 — 修复 Gateway ThreadLocal
- [x] 2.1 删除 `awsome-shop-gateway-service/infrastructure/repository/mysql-impl/src/main/java/.../config/UserContext.java` — N/A（文件不存在）
- [x] 2.2 更新 Gateway 中引用 UserContext 的文件 — N/A（无引用）

### Step 3: US-003 — 实现 @RequireOwnerPermission AOP
- [x] 3.1 在 `awsome-shop-auth-service/common/` 创建 `OwnerPermissionAspect.java` — 已存在
- [x] 3.2 在 `awsome-shop-product-service/common/` 创建 `OwnerPermissionAspect.java` — 已存在
- [x] 3.3 在 `awsome-shop-order-service/common/` 创建 `OwnerPermissionAspect.java` — 已存在
- [x] 3.4 在 `awsome-shop-points-service/common/` 创建 `OwnerPermissionAspect.java` — 已存在
- [x] 3.5 确保各服务 common 模块的 pom.xml 包含 spring-aop + aspectjweaver + spring-context 依赖 — 已添加

### Step 4: US-004 — 清理后端占位代码（5 个服务 × ~19 文件）
- [x] 4.1 清理 auth-service 的 Test* 占位文件（17 个 Java 文件 + 1 个 XML + 1 个 SQL）
- [x] 4.2 清理 product-service 的 Test* 占位文件
- [x] 4.3 清理 order-service 的 Test* 占位文件
- [x] 4.4 清理 points-service 的 Test* 占位文件
- [x] 4.5 清理 gateway-service 的 Test* 占位文件
- [x] 4.6 删除各服务的 `SampleErrorCode.java` — 已删除，product-service 引用替换为新建的 ProductErrorCode

### Step 5: US-004 — 清理前端占位代码
- [x] 5.1 删除 `awsome-shop-frontend/src/components/Layout/index.tsx`
- [x] 5.2 删除 `awsome-shop-frontend/src/components/Layout/AppHeader.tsx`
- [x] 5.3 删除 `awsome-shop-frontend/src/components/Layout/Sidebar.tsx`
- [x] 5.4 删除 `awsome-shop-frontend/src/pages/Home/index.tsx`
- [x] 5.5 清理前端中对已删除组件的任何引用 — 无引用需清理

### Step 6: 验证编译（EC2 远程编译）
- [x] 6.1 验证 auth-service 编译通过 ✅
- [x] 6.2 验证 product-service 编译通过 ✅
- [x] 6.3 验证 order-service 编译通过 ✅
- [x] 6.4 验证 points-service 编译通过 ✅
- [x] 6.5 验证 gateway-service 编译通过 ✅
- [x] 6.6 验证前端 build 通过 ✅

### Step 7: 文档
- [x] 7.1 创建 `construction/tech-debt/code/code-summary.md` 记录变更摘要
