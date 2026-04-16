-- 管理员账号 (admin / admin123)
INSERT INTO `user` (`username`, `password_hash`, `display_name`, `role`) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '系统管理员', 'admin');

-- 示例员工账号 (employee / emp123)
INSERT INTO `user` (`username`, `password_hash`, `display_name`, `role`) VALUES
('employee', '$2a$10$dXJ3SW6G7P50lGmMQgel6uVktDQd2BZjvCgSc2XA.jC3IOoMGQ2Z6', '示例员工', 'employee');
