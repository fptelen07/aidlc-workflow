# Unit 2: Auth Service — 业务规则

## 注册规则

| 规则 ID | 规则描述 | 校验时机 |
|---------|----------|----------|
| REG-001 | 用户名不能为空 | 参数校验 |
| REG-002 | 用户名必须唯一 | 领域服务 |
| REG-003 | 密码不能为空 | 参数校验 |
| REG-004 | 密码长度 >= 6 位 | 参数校验 |
| REG-005 | 显示名称不能为空 | 参数校验 |
| REG-006 | 新用户默认角色为 employee | 领域服务 |
| REG-007 | 新用户默认状态为 active | 领域服务 |
| REG-008 | 密码使用 BCrypt 加密存储 | 领域服务 |

## 登录规则

| 规则 ID | 规则描述 | 校验时机 |
|---------|----------|----------|
| LOGIN-001 | 用户名和密码不能为空 | 参数校验 |
| LOGIN-002 | 用户必须存在 | 领域服务 |
| LOGIN-003 | 密码必须匹配 BCrypt hash | 领域服务 |
| LOGIN-004 | 登录失败统一返回 AUTH_001（不区分用户不存在/密码错误） | 领域服务 |
| LOGIN-005 | 成功返回 access token + refresh token + 用户信息 | 应用服务 |

## Token 规则

| 规则 ID | 规则描述 | 校验时机 |
|---------|----------|----------|
| TOKEN-001 | Access token 有效期 2 小时 | Token 生成 |
| TOKEN-002 | Refresh token 有效期 7 天 | Token 生成 |
| TOKEN-003 | Token 使用 HS256 签名 | Token 生成 |
| TOKEN-004 | Access token claims: userId, username, role, exp | Token 生成 |
| TOKEN-005 | Refresh token claims: userId, type="refresh", exp | Token 生成 |
| TOKEN-006 | 刷新时验证 refresh token 有效性 | Token 验证 |
| TOKEN-007 | 刷新时确认用户仍然存在且活跃 | 应用服务 |
| TOKEN-008 | 验证接口返回 userId 和 role | Token 验证 |

## 用户信息规则

| 规则 ID | 规则描述 | 校验时机 |
|---------|----------|----------|
| USER-001 | 获取当前用户通过 operatorId（Gateway 注入） | 应用服务 |
| USER-002 | 用户不存在时返回 NOT_FOUND_001 | 领域服务 |
| USER-003 | 积分余额不在 Auth 存储，前端单独查询 Points Service | 设计约束 |
