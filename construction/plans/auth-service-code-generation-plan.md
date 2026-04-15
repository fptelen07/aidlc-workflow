# Code Generation Plan — Unit 2: Auth Service

## Unit Context
- **Unit**: Auth Service (认证服务)
- **Stories**: US-005 (用户注册), US-006 (用户登录与 JWT), US-007 (Token 验证与用户信息)
- **Dependencies**: Unit 1 (Tech Debt) — completed ✅
- **Scope**: auth-service 后端全栈（domain → application → infrastructure → interface → migration）

---

## Execution Steps

### Step 1: 领域模型层 (domain-model)
- [x] 1.1 创建 UserEntity.java — User 聚合根
- [x] 1.2 创建 TokenClaims.java — Token Claims 值对象

### Step 2: 领域服务接口 (domain-api)
- [x] 2.1 创建 UserDomainService.java

### Step 3: 安全服务接口 (security-api)
- [x] 3.1 创建 TokenService.java
- [x] 3.2 创建 PasswordHashService.java

### Step 4: 仓储接口 (repository-api)
- [x] 4.1 创建 UserRepository.java

### Step 5: 领域服务实现 (domain-impl)
- [x] 5.1 创建 UserDomainServiceImpl.java

### Step 6: 错误码 (common)
- [x] 6.1 创建 AuthErrorCode.java

### Step 7: 应用层 DTO (application-api)
- [x] 7.1~7.9 创建 9 个 Request/Response/DTO 文件

### Step 8: 应用服务接口与实现 (application)
- [x] 8.1 创建 AuthApplicationService.java
- [x] 8.2 创建 AuthApplicationServiceImpl.java

### Step 9: JWT 实现 (infrastructure/security/jwt-impl)
- [x] 9.1 创建 JwtTokenServiceImpl.java
- [x] 9.2 创建 BcryptPasswordHashServiceImpl.java
- [x] 9.3 创建 JwtProperties.java

### Step 10: MySQL 仓储实现 (infrastructure/repository/mysql-impl)
- [x] 10.1 创建 UserPO.java
- [x] 10.2 创建 UserMapper.java
- [x] 10.3 创建 UserRepositoryImpl.java
- [x] 10.4 创建 UserMapper.xml

### Step 11: Controller (interface/interface-http)
- [x] 11.1 创建 AuthController.java — 5 个 API 端点

### Step 12: 数据库迁移 (bootstrap)
- [x] 12.1 创建 V2__create_user_table.sql
- [x] 12.2 创建 V3__seed_users.sql

### Step 13: 配置更新 (bootstrap)
- [x] 13.1 更新 application-local.yml — 添加 jwt.* 配置

### Step 14: POM 依赖更新
- [x] 14.1 domain-model/pom.xml — Lombok 已存在 ✅
- [x] 14.2 domain-impl/pom.xml — 依赖已完整 ✅
- [x] 14.3 application-impl/pom.xml — 添加 security-api 依赖
- [x] 14.4 jwt-impl/pom.xml — 添加 spring-security-crypto + common 依赖

### Step 15: 验证编译
- [x] 15.1 打包上传到 EC2
- [x] 15.2 auth-service `mvn clean compile` 通过 ✅

### Step 16: 文档
- [x] 16.1 创建 code-summary.md
