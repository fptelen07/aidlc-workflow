# Unit 1: 技术债务 — 功能设计

## 业务逻辑模型

Unit 1 不涉及业务逻辑，是纯技术重构。

---

## US-001: 统一 Result 响应类

### 当前问题
- `common.result.Result<T>` — String code ("SUCCESS"), String message, T data
- `facade.http.response.Result<T>` — Integer code (0=success), String message, T data
- TestController 使用 common 版本，GlobalExceptionHandler 使用 facade 版本

### 设计方案
**保留 `facade.http.response.Result<T>`（Integer code 版本）作为统一标准**，删除 `common.result.Result<T>`。

理由：Integer code 更适合 HTTP 响应（0=成功，非零=错误码），与 GlobalExceptionHandler 的错误码映射逻辑一致。

### 变更清单
1. 删除 `common/src/.../common/result/Result.java`
2. 所有服务的控制器统一使用 `facade.http.response.Result<T>`
3. 更新 `common/src/.../common/dto/PageResult.java` 中对 Result 的引用（如有）
4. 确保 GlobalExceptionHandler 中的 Result 引用不变

### 统一后的 Result 规范
```
Result<T>:
  code: Integer    — 0=成功, 非零=错误码 (如 401001, 404001)
  message: String  — 描述信息
  data: T          — 响应数据

静态工厂:
  Result.success()           → {code: 0, message: "SUCCESS", data: null}
  Result.success(data)       → {code: 0, message: "SUCCESS", data: data}
  Result.error(code, msg)    → {code: code, message: msg, data: null}
```

---

## US-002: 修复 Gateway ThreadLocal

### 当前问题
- Gateway 使用 WebFlux（响应式），但 `UserContext` 使用 `ThreadLocal<Long>`
- 响应式环境中请求可能跨线程，ThreadLocal 不可靠

### 设计方案
- **Gateway 服务**: 删除 `UserContext.java`，Gateway 不需要本地 CRUD（Test 代码将被清理）
- **其他 4 个 Servlet 服务**: 保留 `UserContext.java`，Servlet 环境中 ThreadLocal 正常工作
- 后续 4 个服务的 UserContext 将通过 Gateway 注入的 operatorId 设置（在 Controller 层或 Filter 中从请求体提取）

### 变更清单
1. 删除 `awsome-shop-gateway-service/infrastructure/repository/mysql-impl/.../config/UserContext.java`
2. 其他 4 个服务保留 UserContext 不变
3. 后续各服务需在 Controller 层或 Interceptor 中从请求体的 operatorId 字段设置 UserContext

---

## US-003: 实现 @RequireOwnerPermission AOP

### 当前问题
- `@RequireOwnerPermission` 注解已定义（resourceIdParam, allowAdmin），但无 AOP 切面

### 设计方案
在每个 Servlet 服务的 common 或 application-impl 模块中创建 AOP 切面。

### AOP 切面逻辑
```
@Around("@annotation(requireOwnerPermission)")
1. 从 UserContext 获取当前用户 ID
2. 如果 allowAdmin=true 且用户角色为 ADMIN → 放行
3. 从方法参数中提取 resourceIdParam 指定的参数值
4. 比较当前用户 ID 与资源所有者 ID
5. 不匹配 → 抛出 BusinessException(AUTHZ_001, "无权操作此资源")
6. 匹配 → 放行
```

### 变更清单
1. 在 common 模块添加 `OwnerPermissionAspect.java`
2. 需要依赖 UserContext 获取当前用户
3. 需要依赖 Spring AOP（已在 spring-boot-starter 中）

---

## US-004: 清理占位代码

### 后端清理清单（每个服务）
删除以下 Test 占位文件：
- `domain/domain-model/.../model/test/TestEntity.java`
- `domain/domain-api/.../service/test/TestDomainService.java`
- `domain/domain-impl/.../impl/service/test/TestDomainServiceImpl.java`
- `domain/repository-api/.../repository/test/TestRepository.java`
- `application/application-api/.../api/dto/test/TestDTO.java`
- `application/application-api/.../api/dto/test/request/Create|Update|Delete|Get|ListTestRequest.java`
- `application/application-api/.../api/service/test/TestApplicationService.java`
- `application/application-impl/.../impl/service/test/TestApplicationServiceImpl.java`
- `interface/interface-http/.../controller/TestController.java`
- `infrastructure/repository/mysql-impl/.../impl/test/TestRepositoryImpl.java`
- `infrastructure/repository/mysql-impl/.../mapper/test/TestMapper.java` + `TestMapper.xml`
- `infrastructure/repository/mysql-impl/.../po/test/TestPO.java`
- `common/.../enums/SampleErrorCode.java`
- `bootstrap/src/main/resources/db/migration/V1__create_test_table.sql`

### 前端清理清单
- `src/components/Layout/index.tsx`（未使用的通用 Layout）
- `src/components/Layout/AppHeader.tsx`（未使用）
- `src/components/Layout/Sidebar.tsx`（未使用）
- `src/pages/Home/index.tsx`（未使用的占位页面）

### 验证
- 清理后所有 5 个后端服务 `mvn clean compile` 成功
- 前端 `npm run build` 成功
