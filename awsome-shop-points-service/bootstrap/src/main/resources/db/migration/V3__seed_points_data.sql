-- =====================================================
-- V3: 积分服务种子数据
-- =====================================================

-- 示例员工积分账户 (employee userId=2, balance=2580)
INSERT INTO points_account (user_id, balance) VALUES (2, 2580);

-- 示例管理员积分账户 (admin userId=1, balance=10000)
INSERT INTO points_account (user_id, balance) VALUES (1, 10000);

-- 示例积分交易记录
INSERT INTO points_transaction (user_id, type, amount, reason) VALUES
(2, 'grant', 1000, '新员工注册奖励'),
(2, 'grant', 2000, '月度绩效奖励'),
(2, 'deduct', 420, '兑换商品'),
(1, 'grant', 10000, '系统初始化');

-- 示例积分规则
INSERT INTO points_rule (name, type, amount, enabled, description) VALUES
('新员工注册奖励', 'register', 1000, 1, '新员工注册时自动发放1000积分'),
('月度绩效奖励', 'monthly', 2000, 1, '每月绩效考核优秀员工发放2000积分'),
('生日奖励', 'birthday', 500, 1, '员工生日当天发放500积分');
