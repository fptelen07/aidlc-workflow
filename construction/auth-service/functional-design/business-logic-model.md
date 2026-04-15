# Unit 2: Auth Service — 功能设计

## 业务逻辑模型

Auth Service 负责用户注册、登录认证、JWT Token 管理和用户信息查询。

---

## US-005: 用户注册

### 业务流程
```
RegisterRequest(username, password, displayName)
  → 参数校验（非空、密码 >= 6 位）
  → 用户名唯一性校验
  → 密码 BCrypt 加密
  → 创建 User（role=employee, status=active）
  → 返回成功
```

### 业务规则
- 用户名：非空，唯一
- 密码：最少 6 位，BCrypt 加密存储
- 显示名称：非空
- 默认角色：`employee`
- 初始积分：0（由 Points Service 管理，Auth 不存储积分）
- 注册不自动登录，需跳转登录页

### 错误场景
| 场景 | 错误码 | 消息 |
|------|--------|------|
| 用户名已存在 | CONFLICT_001 | 用户名已存在: {username} |
| 密码少于 6 位 | PARAM_001 | 密码长度不能少于6位 |
| 用户名为空 | PARAM_002 | 用户名不能为空 |
| 显示名称为空 | PARAM_003 | 显示名称不能为空 |

---

## US-006: 用户登录与 JWT Token

### 业务流程
```
LoginRequest(username, password)
  → 查找用户（by username）
  → 验证密码（BCrypt verify）
  → 生成 access token（2h 有效期）
  → 生成 refresh token（7d 有效期）
  → 返回 LoginResponse(accessToken, refreshToken, user)
```

### Token 规范
| 属性 | Access Token | Refresh Token |
|------|-------------|---------------|
| 有效期 | 2 小时 | 7 天 |
| Claims | userId, username, role, exp | userId, type="refresh", exp |
| 用途 | API 认证 | 刷新 access token |

### Token 刷新流程
```
RefreshTokenRequest(refreshToken)
  → 验证 refresh token 有效性
  → 提取 userId
  → 查找用户（确认存在且活跃）
  → 生成新 access token
  → 返回 TokenResponse(accessToken)
```

### 错误场景
| 场景 | 错误码 | 消息 |
|------|--------|------|
| 用户名或密码错误 | AUTH_001 | 用户名或密码错误 |
| 用户不存在 | AUTH_001 | 用户名或密码错误 |
| Refresh token 过期 | AUTH_002 | Token 已过期，请重新登录 |
| Refresh token 无效 | AUTH_003 | 无效的 Token |

---

## US-007: Token 验证与用户信息

### Token 验证流程（内部接口，Gateway 调用）
```
ValidateRequest(token)
  → 验证 JWT 签名和有效期
  → 提取 claims（userId, role）
  → 返回 ValidateResponse(valid=true, userId, role)
```

### 获取当前用户信息
```
GetCurrentUserRequest(operatorId)  // operatorId 由 Gateway 注入
  → 查找用户（by id）
  → 返回 UserDTO(id, username, displayName, role, avatar, createdAt)
```

注意：积分余额不在 Auth Service 存储，前端需单独调用 Points Service 获取。

### 错误场景
| 场景 | 错误码 | 消息 |
|------|--------|------|
| Token 无效 | AUTH_003 | 无效的 Token |
| Token 过期 | AUTH_002 | Token 已过期 |
| 用户不存在 | NOT_FOUND_001 | 用户不存在 |

---

## 领域模型

### User Entity
```
User:
  id: Long (PK, auto-increment)
  username: String (unique, not null)
  passwordHash: String (not null, BCrypt)
  displayName: String (not null)
  role: String (not null, "employee" | "admin")
  avatar: String (nullable, URL)
  status: String (not null, "active" | "disabled", default "active")
  createdAt: LocalDateTime
  updatedAt: LocalDateTime
```

### TokenClaims (Value Object)
```
TokenClaims:
  userId: Long
  username: String
  role: String
  type: String ("access" | "refresh")
  expiresAt: LocalDateTime
```

---

## API 端点设计

| 端点 | 方法 | 认证 | 描述 |
|------|------|------|------|
| /api/v1/public/auth/register | POST | 无 | 用户注册 |
| /api/v1/public/auth/login | POST | 无 | 用户登录 |
| /api/v1/public/auth/refresh | POST | 无 | 刷新 token |
| /api/v1/internal/auth/validate | POST | 无（内部） | Gateway 验证 token |
| /api/v1/auth/me | POST | 需认证 | 获取当前用户信息 |

### Request/Response 模型

#### RegisterRequest
```json
{ "username": "string", "password": "string", "displayName": "string" }
```

#### LoginRequest / LoginResponse
```json
// Request
{ "username": "string", "password": "string" }
// Response.data
{ "accessToken": "string", "refreshToken": "string", "user": UserDTO }
```

#### RefreshTokenRequest / TokenResponse
```json
// Request
{ "refreshToken": "string" }
// Response.data
{ "accessToken": "string" }
```

#### ValidateRequest / ValidateResponse
```json
// Request
{ "token": "string" }
// Response.data
{ "valid": true, "userId": 1, "role": "employee" }
```

#### UserDTO
```json
{ "id": 1, "username": "string", "displayName": "string", "role": "employee", "avatar": "url", "createdAt": "2026-04-15T10:00:00" }
```

---

## Flyway 迁移（US-024 Auth 部分）

### V2__create_user_table.sql
```sql
CREATE TABLE IF NOT EXISTS `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `password_hash` VARCHAR(255) NOT NULL,
  `display_name` VARCHAR(100) NOT NULL,
  `role` VARCHAR(20) NOT NULL DEFAULT 'employee',
  `avatar` VARCHAR(500) DEFAULT NULL,
  `status` VARCHAR(20) NOT NULL DEFAULT 'active',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### V3__seed_users.sql
```sql
-- 管理员 (admin / admin123, BCrypt hash)
INSERT INTO `user` (username, password_hash, display_name, role) VALUES
('admin', '$2a$10$...', '系统管理员', 'admin');

-- 示例员工 (employee / emp123, BCrypt hash)
INSERT INTO `user` (username, password_hash, display_name, role) VALUES
('employee', '$2a$10$...', '示例员工', 'employee');
```
注：BCrypt hash 在代码生成阶段生成实际值。

---

## 安全设计

### JWT 配置
- 签名算法：HS256
- 密钥：从 application.yml 配置读取（`jwt.secret`）
- Access token 有效期：7200 秒（2 小时）
- Refresh token 有效期：604800 秒（7 天）

### 密码安全
- BCrypt 加密（Spring Security 的 BCryptPasswordEncoder）
- 不存储明文密码
- 登录失败不区分"用户不存在"和"密码错误"（统一返回 AUTH_001）
