# Unit 1: Tech Debt — Code Generation Summary

## 变更概要

### US-001: 统一 Result 类
- `common.result.Result` 文件在所有 5 个服务中均不存在（仅 `facade.http.response.Result` 存在）
- 唯一引用在 TestController 中，随 US-004 一起清理

### US-002: 修复 Gateway ThreadLocal
- Gateway 的 `UserContext.java` 不存在（其他 4 个 Servlet 服务保留）
- 无需修改

### US-003: 实现 @RequireOwnerPermission AOP
- 4 个 Servlet 服务的 `OwnerPermissionAspect.java` 已存在
- 修复：4 个服务的 `common/pom.xml` 添加 `spring-aop` + `aspectjweaver` + `spring-context` 依赖

### US-004: 清理占位代码
**后端（每个服务 19 个文件，共 95 个文件）**:
- 删除 TestEntity, TestDomainService, TestDomainServiceImpl
- 删除 TestRepository, TestRepositoryImpl, TestMapper.java, TestMapper.xml, TestPO
- 删除 TestDTO, Create/Update/Delete/Get/ListTestRequest
- 删除 TestApplicationService, TestApplicationServiceImpl
- 删除 TestController
- 删除 SampleErrorCode.java
- 删除 V1__create_test_table.sql
- 新建 `ProductErrorCode.java` 替代 product-service 中的 SampleErrorCode 引用

**前端（4 个文件）**:
- 删除 Layout/index.tsx, AppHeader.tsx, Sidebar.tsx
- 删除 pages/Home/index.tsx

## 编译验证
- 5 个后端服务 `mvn clean compile` 全部通过 ✅
- 前端 `npm run build` 通过 ✅
- 验证环境：EC2 (ec2-54-89-171-32.compute-1.amazonaws.com)
