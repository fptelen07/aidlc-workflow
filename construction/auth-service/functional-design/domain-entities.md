# Unit 2: Auth Service — 领域实体

## User (聚合根)

```
User:
  id: Long                    — 主键，自增
  username: String            — 用户名，唯一，非空，最长 50 字符
  passwordHash: String        — BCrypt 加密密码，非空
  displayName: String         — 显示名称，非空，最长 100 字符
  role: String                — 角色 ("employee" | "admin")，非空
  avatar: String              — 头像 URL，可空
  status: String              — 状态 ("active" | "disabled")，默认 "active"
  createdAt: LocalDateTime    — 创建时间
  updatedAt: LocalDateTime    — 更新时间
```

### 行为
- `register(username, passwordHash, displayName)` → 创建新用户
- `authenticate(rawPassword)` → 验证密码是否匹配

## TokenClaims (值对象)

```
TokenClaims:
  userId: Long                — 用户 ID
  username: String            — 用户名
  role: String                — 角色
  type: String                — Token 类型 ("access" | "refresh")
  expiresAt: LocalDateTime    — 过期时间
```

## 服务接口

### UserDomainService
- `register(username, passwordHash, displayName): User`
- `authenticate(username, password): User`
- `findById(id): User`
- `findByUsername(username): User`

### TokenService (Security SPI)
- `generateAccessToken(userId, username, role): String`
- `generateRefreshToken(userId): String`
- `validateToken(token): TokenClaims`

### PasswordHashService (Security SPI)
- `hash(rawPassword): String`
- `verify(rawPassword, hashedPassword): boolean`

### UserRepository (Repository SPI)
- `save(user): User`
- `findById(id): User`
- `findByUsername(username): User`
- `existsByUsername(username): boolean`

## 数据库表映射

| 实体字段 | 数据库列 | 类型 |
|----------|----------|------|
| id | id | BIGINT AUTO_INCREMENT |
| username | username | VARCHAR(50) UNIQUE |
| passwordHash | password_hash | VARCHAR(255) |
| displayName | display_name | VARCHAR(100) |
| role | role | VARCHAR(20) |
| avatar | avatar | VARCHAR(500) |
| status | status | VARCHAR(20) |
| createdAt | created_at | DATETIME |
| updatedAt | updated_at | DATETIME |
