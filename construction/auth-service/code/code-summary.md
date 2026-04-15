# Unit 2: Auth Service — Code Generation Summary

## 新建文件（28 个）

### Domain Layer
- `domain/domain-model/.../model/user/UserEntity.java` — User 聚合根
- `domain/domain-model/.../model/auth/TokenClaims.java` — Token Claims 值对象
- `domain/domain-api/.../service/user/UserDomainService.java` — 用户领域服务接口
- `domain/domain-impl/.../service/user/UserDomainServiceImpl.java` — 用户领域服务实现
- `domain/security-api/.../service/TokenService.java` — JWT Token 服务接口
- `domain/security-api/.../service/PasswordHashService.java` — 密码哈希服务接口
- `domain/repository-api/.../repository/user/UserRepository.java` — 用户仓储接口

### Application Layer
- `application/application-api/.../dto/auth/request/LoginRequest.java`
- `application/application-api/.../dto/auth/request/RegisterRequest.java`
- `application/application-api/.../dto/auth/request/RefreshTokenRequest.java`
- `application/application-api/.../dto/auth/request/ValidateRequest.java`
- `application/application-api/.../dto/auth/request/GetCurrentUserRequest.java`
- `application/application-api/.../dto/auth/response/LoginResponse.java`
- `application/application-api/.../dto/auth/response/TokenResponse.java`
- `application/application-api/.../dto/auth/response/ValidateResponse.java`
- `application/application-api/.../dto/auth/UserDTO.java`
- `application/application-api/.../service/auth/AuthApplicationService.java`
- `application/application-impl/.../service/auth/AuthApplicationServiceImpl.java`

### Infrastructure Layer
- `infrastructure/security/jwt-impl/.../jwt/JwtProperties.java`
- `infrastructure/security/jwt-impl/.../jwt/JwtTokenServiceImpl.java`
- `infrastructure/security/jwt-impl/.../jwt/BcryptPasswordHashServiceImpl.java`
- `infrastructure/repository/mysql-impl/.../po/user/UserPO.java`
- `infrastructure/repository/mysql-impl/.../mapper/user/UserMapper.java`
- `infrastructure/repository/mysql-impl/.../mapper/user/UserMapper.xml`
- `infrastructure/repository/mysql-impl/.../impl/user/UserRepositoryImpl.java`

### Interface Layer
- `interface/interface-http/.../controller/AuthController.java` — 5 个 API 端点

### Common
- `common/.../enums/AuthErrorCode.java`

### Database Migration
- `bootstrap/.../db/migration/V2__create_user_table.sql`
- `bootstrap/.../db/migration/V3__seed_users.sql`

## 修改文件（3 个）
- `application/application-impl/pom.xml` — 添加 security-api 依赖
- `infrastructure/security/jwt-impl/pom.xml` — 添加 spring-security-crypto + common 依赖
- `bootstrap/.../application-local.yml` — 添加 jwt.* 配置

## 编译验证
- `mvn clean compile` 通过 ✅（EC2 远程验证）
