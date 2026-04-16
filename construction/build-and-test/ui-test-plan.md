# AWSome Shop — UI 测试计划

## 测试环境
- URL: https://d34mclf1q3xzvo.cloudfront.net
- 工具: Playwright MCP
- 测试账号: employee / emp123, admin / admin123

---

## TC-01: 员工登录
1. 打开 /login
2. 输入 employee / emp123
3. 点击登录
4. 验证：跳转到首页，右上角显示积分

## TC-02: 分类筛选
1. 在首页点击"数码电子"
2. 验证：只显示数码电子类商品
3. 点击"All"
4. 验证：显示所有商品

## TC-03: 商品详情 + 下单
1. 点击任意商品进入详情页
2. 验证：显示商品名、积分价格、库存、Redeem Now 按钮
3. 点击 Redeem Now
4. 验证：弹出确认弹窗
5. 点击 CONFIRM
6. 验证：跳转到 Orders 页面，新订单状态为 Pending

## TC-04: 订单列表筛选
1. 在 Orders 页面点击 PENDING
2. 验证：只显示 Pending 状态订单
3. 点击 ALL
4. 验证：显示所有订单

## TC-05: 积分中心
1. 点击导航栏 Points
2. 验证：显示积分余额（数字 > 0）
3. 验证：显示积分变动历史列表

## TC-06: 退出登录
1. 点击右上角头像
2. 点击退出登录
3. 验证：跳转到 /login

## TC-07: Admin 登录
1. 输入 admin / admin123
2. 点击登录
3. 验证：跳转到 /admin（Dashboard）

## TC-08: Admin 订单管理
1. 点击左侧 Orders
2. 验证：显示订单列表，有 Pending 订单
3. 点击 Confirm 按钮
4. 验证：订单状态变为 Completed

## TC-09: Admin 商品管理
1. 点击左侧 Products
2. 验证：显示商品列表

## TC-10: Admin 退出
1. 点击头像 → 退出
2. 验证：跳转到 /login
