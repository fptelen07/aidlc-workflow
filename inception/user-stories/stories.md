# 用户故事 (User Stories)

按服务分组，粗粒度，按依赖顺序排列。

---

## 一、技术债务与基础设施 (Tech Debt & Infrastructure)

### US-001: 统一 Result 响应类
**作为** 开发者，**我需要** 统一 `common.result.Result<T>` 和 `facade.http.response.Result<T>` 为一个版本，**以便** 所有服务的 API 响应格式一致。

**验收标准**:
- Given 系统中存在两个 Result 类，When 统一为一个版本后，Then 所有控制器和异常处理器使用同一个 Result 类
- Given 统一后的 Result 类，When 返回成功响应，Then code=0, message="SUCCESS"
- Given 统一后的 Result 类，When 返回错误响应，Then code 为对应错误码整数值

### US-002: 修复 Gateway ThreadLocal 问题
**作为** 开发者，**我需要** 移除 Gateway 服务中基于 ThreadLocal 的 UserContext，**以便** 在 WebFlux 响应式环境中正确运行。

**验收标准**:
- Given Gateway 使用 WebFlux，When 移除 ThreadLocal UserContext，Then Gateway 的 Test CRUD 路径不再依赖 ThreadLocal
- Given 其他 4 个 Servlet 服务，When 保留 ThreadLocal UserContext，Then 非响应式服务不受影响

### US-003: 实现 @RequireOwnerPermission AOP
**作为** 开发者，**我需要** 为 `@RequireOwnerPermission` 注解实现 AOP 切面，**以便** 资源所有者权限校验自动生效。

**验收标准**:
- Given 方法标注 @RequireOwnerPermission，When 非资源所有者调用，Then 抛出 403 异常
- Given allowAdmin=true，When 管理员调用，Then 允许访问

### US-004: 清理占位代码
**作为** 开发者，**我需要** 删除所有 Test 占位代码和前端未使用组件，**以便** 代码库干净整洁。

**验收标准**:
- Given 所有服务中的 Test* 文件，When 清理完成，Then 无 TestEntity/TestController/TestMapper 等文件残留
- Given 前端未使用组件（Layout/index.tsx, AppHeader, Sidebar, Home），When 清理完成，Then 无未使用组件残留
- Given 清理后，When 编译所有服务，Then 编译成功无错误

---

## 二、认证服务 (Auth Service)

### US-005: 用户注册
**作为** 员工，**我需要** 通过用户名、密码和显示名称注册账号，**以便** 获得商城使用权限。

**验收标准**:
- Given 注册页面，When 填写用户名/密码/显示名称并提交，Then 创建 employee 角色账号，初始积分为 0
- Given 已存在的用户名，When 尝试注册，Then 返回用户名已存在错误
- Given 密码少于 6 位，When 提交注册，Then 返回密码强度不足错误
- Given 注册成功，When 跳转登录页，Then 可以使用新账号登录

### US-006: 用户登录与 JWT Token
**作为** 用户，**我需要** 使用用户名和密码登录，**以便** 获取 JWT token 访问系统功能。

**验收标准**:
- Given 正确的用户名和密码，When 登录，Then 返回 access token（2h 有效期）+ refresh token（7d 有效期）+ 用户信息
- Given 错误的密码，When 登录，Then 返回认证失败错误
- Given access token 过期，When 使用 refresh token 刷新，Then 返回新的 access token
- Given refresh token 过期，When 尝试刷新，Then 返回需要重新登录错误

### US-007: Token 验证与用户信息
**作为** Gateway，**我需要** 调用 Auth 内部接口验证 token，**以便** 确认请求者身份并注入 operatorId。

**验收标准**:
- Given 有效的 JWT token，When 调用 /api/v1/internal/auth/validate，Then 返回 success=true 和 operatorId
- Given 无效/过期的 token，When 调用验证接口，Then 返回 success=false 和错误消息
- Given 已认证用户，When 调用获取当前用户信息接口，Then 返回完整用户信息（含积分余额）

### US-008: 前端注册页面
**作为** 员工，**我需要** 在前端注册页面（/register）填写信息注册，**以便** 自助创建账号。

**验收标准**:
- Given 注册页面，When 加载，Then 显示用户名、密码、确认密码、显示名称表单
- Given 两次密码不一致，When 提交，Then 前端提示密码不匹配
- Given 注册成功，When 提交，Then 跳转到登录页并提示注册成功
- Given 注册页面，When 切换语言，Then 所有文本切换为对应语言

### US-009: 前端登录对接真实 API
**作为** 用户，**我需要** 前端登录页对接真实认证 API，**以便** 替换 Mock 数据实现真实登录。

**验收标准**:
- Given 前端登录页，When 输入正确凭据提交，Then 调用后端 API 获取 token 并存储
- Given 登录成功，When 根据角色跳转，Then employee 跳转 /，admin 跳转 /admin
- Given token 过期，When 发起请求，Then 自动尝试 refresh，失败则跳转登录页

---

## 三、商品服务 (Product Service)

### US-010: 商品与分类管理（后端）
**作为** 管理员，**我需要** 通过 API 管理商品和分类，**以便** 维护商城商品目录。

**验收标准**:
- Given 管理员身份，When 创建商品（名称/描述/分类/积分价格/库存/图片URL/状态），Then 商品保存成功
- Given 商品列表，When 按名称搜索、按分类筛选、分页查询，Then 返回匹配结果
- Given 商品，When 编辑/删除/上架/下架，Then 操作成功并更新状态
- Given 分类，When CRUD 操作，Then 操作成功；有商品关联时禁止删除
- Given Flyway 迁移，When 服务启动，Then 自动创建 product/category 表并初始化默认分类和示例商品

### US-011: 商品浏览与详情（后端）
**作为** 员工，**我需要** 浏览和搜索已上架商品，**以便** 找到想兑换的商品。

**验收标准**:
- Given 员工身份，When 查询商品列表，Then 仅返回已上架且库存 > 0 的商品
- Given 商品 ID，When 查询商品详情，Then 返回完整商品信息

### US-012: 管理端商品管理页面
**作为** 管理员，**我需要** 在管理端页面（/admin/products）管理商品，**以便** 通过界面操作商品上下架和编辑。

**验收标准**:
- Given 商品管理页，When 加载，Then 显示商品数据表格（分页、搜索、分类筛选）
- Given 新建/编辑弹窗，When 填写信息并提交，Then 商品创建/更新成功
- Given 商品行，When 点击上架/下架切换，Then 状态即时更新
- Given 商品行，When 点击删除并确认，Then 商品逻辑删除

### US-013: 管理端分类管理页面
**作为** 管理员，**我需要** 在管理端页面（/admin/categories）管理分类，**以便** 维护商品分类体系。

**验收标准**:
- Given 分类管理页，When 加载，Then 显示分类列表
- Given 新建/编辑弹窗，When 填写名称和排序并提交，Then 分类创建/更新成功
- Given 有商品关联的分类，When 尝试删除，Then 提示无法删除

### US-014: 员工端商品详情页与商城对接
**作为** 员工，**我需要** 查看商品详情页（/product/:id）并在商城首页浏览真实商品，**以便** 了解商品信息后决定是否兑换。

**验收标准**:
- Given 商品详情页，When 加载，Then 显示商品图片、名称、描述、积分价格、库存、分类
- Given 积分不足，When 查看兑换按钮，Then 按钮禁用并提示积分不足
- Given ShopHome 页面，When 加载，Then 调用真实 API 获取商品列表替换 Mock 数据
- Given 分类筛选，When 选择分类，Then 调用 API 按分类过滤商品

---

## 四、订单服务 (Order Service)

### US-015: 兑换订单创建与管理（后端）
**作为** 系统，**我需要** 处理兑换订单的创建和状态流转，**以便** 完成积分兑换业务闭环。

**验收标准**:
- Given 员工发起兑换，When 商品存在且上架、库存充足、积分充足，Then 创建 pending 订单（不扣积分）
- Given 商品库存不足或积分不足，When 发起兑换，Then 返回对应错误
- Given pending 订单，When 管理员确认，Then 状态变为 completed，调用积分服务扣减积分，调用商品服务扣减库存
- Given pending 订单，When 管理员拒绝，Then 状态变为 rejected，不扣减积分
- Given 员工，When 查询自己的订单列表，Then 仅返回自己的订单（分页、按状态筛选）
- Given 管理员，When 查询订单列表，Then 返回所有订单（分页、按状态筛选、按用户搜索）
- Given Flyway 迁移，When 服务启动，Then 自动创建 order 表

### US-016: 管理端订单管理页面
**作为** 管理员，**我需要** 在管理端页面（/admin/orders）处理兑换订单，**以便** 及时确认或拒绝员工的兑换请求。

**验收标准**:
- Given 订单管理页，When 加载，Then 显示订单表格（分页、状态筛选）
- Given pending 订单，When 点击确认，Then 订单状态变为 completed
- Given pending 订单，When 点击拒绝，Then 订单状态变为 rejected
- Given 订单行，When 点击查看详情，Then 显示订单完整信息

### US-017: 员工端兑换记录页面
**作为** 员工，**我需要** 在兑换记录页面（/orders）查看我的兑换历史，**以便** 了解兑换进度和历史。

**验收标准**:
- Given 兑换记录页，When 加载，Then 显示我的订单列表（商品名、积分、状态、时间）
- Given 订单列表，When 按状态筛选，Then 显示对应状态的订单
- Given 订单列表，When 翻页，Then 正确加载下一页数据

---

## 五、积分服务 (Points Service)

### US-018: 积分账户与交易（后端）
**作为** 系统，**我需要** 管理用户积分账户和交易记录，**以便** 支撑积分发放、扣减和查询业务。

**验收标准**:
- Given 用户，When 查询积分余额，Then 返回当前可用积分
- Given 管理员发放积分，When 指定用户和数量，Then 余额增加并记录交易流水（类型：发放）
- Given 批量发放，When 指定多个用户和数量，Then 所有用户余额增加并分别记录流水
- Given 兑换扣减，When 扣减积分，Then 余额减少并记录交易流水（类型：扣减）；余额不足时拒绝
- Given 用户，When 查询积分变动历史，Then 返回分页列表（按类型筛选）
- Given Flyway 迁移，When 服务启动，Then 自动创建 points_account/points_transaction 表

### US-019: 积分过期与规则配置（后端）
**作为** 管理员，**我需要** 配置积分过期策略和发放规则，**以便** 灵活管理积分体系。

**验收标准**:
- Given 积分有效期配置（默认 12 个月），When 积分到期，Then 自动标记为过期并扣减余额
- Given 发放规则（如新员工注册奖励），When 创建/编辑/启用/禁用规则，Then 规则保存成功
- Given 积分统计，When 管理员查询，Then 返回月度发放量、扣减量、流通量
- Given Flyway 迁移，When 服务启动，Then 自动创建 points_rule 表

### US-020: 管理端积分管理页面
**作为** 管理员，**我需要** 在管理端页面（/admin/points）管理积分，**以便** 发放积分和查看统计。

**验收标准**:
- Given 积分管理页，When 加载，Then 显示积分统计卡片（月度发放量、扣减量、流通量）
- Given 发放表单，When 选择用户并输入数量和原因，Then 积分发放成功
- Given 批量发放，When 选择多个用户，Then 批量发放成功
- Given 积分变动记录，When 查看列表，Then 显示所有用户的积分变动（分页）
- Given 发放规则，When 配置规则，Then 规则保存并可启用/禁用

### US-021: 员工端积分中心页面
**作为** 员工，**我需要** 在积分中心页面（/points）查看积分信息，**以便** 了解积分余额和变动情况。

**验收标准**:
- Given 积分中心页，When 加载，Then 显示当前积分余额
- Given 积分历史列表，When 加载，Then 显示变动记录（类型、数量、原因、时间）
- Given 历史列表，When 按类型筛选（发放/扣减/过期），Then 显示对应类型记录
- Given 历史列表，When 翻页，Then 正确加载下一页

---

## 六、用户管理与仪表盘

### US-022: 管理端用户管理页面
**作为** 管理员，**我需要** 在管理端页面（/admin/users）管理用户，**以便** 查看和编辑用户信息。

**验收标准**:
- Given 用户管理页，When 加载，Then 显示用户表格（分页、搜索）
- Given 用户行，When 查看详情，Then 显示用户完整信息（含积分余额）
- Given 用户行，When 编辑信息，Then 用户信息更新成功

### US-023: Dashboard 对接真实 API
**作为** 管理员，**我需要** Dashboard 页面展示真实统计数据，**以便** 了解商城运营状况。

**验收标准**:
- Given Dashboard 页面，When 加载，Then 调用真实 API 获取统计数据替换 Mock
- Given 统计卡片，When 显示，Then 展示真实的商品总数、用户总数、月度兑换量、积分流通量
- Given 最近订单表格，When 显示，Then 展示真实的最近兑换订单

---

## 七、数据初始化

### US-024: Flyway 数据库迁移与种子数据
**作为** 开发者，**我需要** 通过 Flyway 创建所有业务表并初始化种子数据，**以便** 系统启动后即可使用。

**验收标准**:
- Given Auth 服务启动，When Flyway 执行，Then 创建 user/role 表，插入管理员（admin/admin123）和示例员工（employee/emp123, 2580 积分）
- Given Product 服务启动，When Flyway 执行，Then 创建 product/category 表，插入 5 个默认分类和示例商品
- Given Order 服务启动，When Flyway 执行，Then 创建 order 表
- Given Points 服务启动，When Flyway 执行，Then 创建 points_account/points_transaction/points_rule 表，初始化示例员工积分账户

---

## 需求覆盖矩阵

| 需求编号 | 用户故事 |
|----------|----------|
| FR-001 认证服务 | US-005, US-006, US-007, US-008, US-009 |
| FR-002 商品服务 | US-010, US-011, US-012, US-013, US-014 |
| FR-003 订单服务 | US-015, US-016, US-017 |
| FR-004 积分服务 | US-018, US-019, US-020, US-021 |
| FR-005 前端页面 | US-008, US-009, US-012, US-013, US-014, US-016, US-017, US-020, US-021, US-022, US-023 |
| FR-006 数据初始化 | US-024 |
| FR-007 技术债务 | US-001, US-002, US-003, US-004 |

## 角色映射

| 角色 | 相关故事 |
|------|----------|
| Employee | US-005, US-006, US-008, US-009, US-011, US-014, US-015, US-017, US-021 |
| Admin | US-006, US-007, US-010, US-012, US-013, US-015, US-016, US-019, US-020, US-022, US-023 |
| System | US-015, US-018, US-019 |
| Developer | US-001, US-002, US-003, US-004, US-024 |
