-- 兑换订单表
CREATE TABLE IF NOT EXISTS `redemption_order` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `user_id`       BIGINT       NOT NULL                COMMENT '用户ID',
    `user_name`     VARCHAR(100) NOT NULL DEFAULT ''      COMMENT '用户名',
    `product_id`    BIGINT       NOT NULL                COMMENT '商品ID',
    `product_name`  VARCHAR(200) NOT NULL DEFAULT ''      COMMENT '商品名称',
    `points_amount` INT          NOT NULL DEFAULT 0       COMMENT '积分数量',
    `status`        VARCHAR(20)  NOT NULL DEFAULT 'pending' COMMENT '订单状态: pending/completed/rejected',
    `created_at`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `created_by`    BIGINT                DEFAULT NULL    COMMENT '创建人',
    `updated_by`    BIGINT                DEFAULT NULL    COMMENT '更新人',
    `deleted`       TINYINT      NOT NULL DEFAULT 0       COMMENT '逻辑删除: 0-未删除, 1-已删除',
    `version`       INT          NOT NULL DEFAULT 0       COMMENT '乐观锁版本号',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='兑换订单表';
