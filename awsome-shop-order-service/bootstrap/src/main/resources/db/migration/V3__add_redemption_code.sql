ALTER TABLE `redemption_order` ADD COLUMN `redemption_code` VARCHAR(20) DEFAULT NULL COMMENT '兑换码' AFTER `status`;
ALTER TABLE `redemption_order` ADD COLUMN `user_email` VARCHAR(200) DEFAULT NULL COMMENT '用户邮箱' AFTER `redemption_code`;
