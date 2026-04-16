-- =====================================================
-- V2: 创建积分服务核心表
-- =====================================================

-- 积分账户表
CREATE TABLE IF NOT EXISTS points_account (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT         NOT NULL COMMENT '用户ID',
    balance     BIGINT         NOT NULL DEFAULT 0 COMMENT '积分余额',
    created_at  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='积分账户表';

-- 积分交易表
CREATE TABLE IF NOT EXISTS points_transaction (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT         NOT NULL COMMENT '用户ID',
    type        VARCHAR(20)    NOT NULL COMMENT '交易类型: grant/deduct/expire',
    amount      BIGINT         NOT NULL COMMENT '交易积分数量',
    reason      VARCHAR(255)   DEFAULT NULL COMMENT '交易原因',
    order_id    BIGINT         DEFAULT NULL COMMENT '关联订单ID',
    created_at  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_type (type),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='积分交易表';

-- 积分规则表
CREATE TABLE IF NOT EXISTS points_rule (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100)   NOT NULL COMMENT '规则名称',
    type        VARCHAR(50)    NOT NULL COMMENT '规则类型',
    amount      BIGINT         NOT NULL COMMENT '积分数量',
    enabled     TINYINT(1)     NOT NULL DEFAULT 1 COMMENT '是否启用',
    description VARCHAR(500)   DEFAULT NULL COMMENT '规则描述',
    created_at  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='积分规则表';
