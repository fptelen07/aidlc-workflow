-- 默认分类
INSERT INTO `category` (`name`, `sort_order`) VALUES
('数码电子', 1),
('生活家居', 2),
('美食餐饮', 3),
('礼品卡券', 4),
('办公用品', 5);

-- 示例商品
INSERT INTO `product` (`name`, `sku`, `category`, `brand`, `points_price`, `market_price`, `stock`, `status`, `description`, `image_url`, `subtitle`) VALUES
('无线蓝牙耳机', 'SKU-001', '数码电子', 'SoundMax', 500, 199.00, 50, 1, '高品质无线蓝牙耳机，支持降噪功能', 'https://picsum.photos/400/400?random=1', '降噪 | 长续航 | 高音质'),
('便携保温杯', 'SKU-002', '生活家居', 'ThermoLife', 200, 89.00, 100, 1, '316不锈钢内胆，12小时保温', 'https://picsum.photos/400/400?random=2', '316不锈钢 | 12h保温'),
('星巴克礼品卡 100元', 'SKU-003', '礼品卡券', 'Starbucks', 300, 100.00, 200, 1, '星巴克电子礼品卡，全国门店通用', 'https://picsum.photos/400/400?random=3', '全国通用 | 即时到账'),
('机械键盘', 'SKU-004', '数码电子', 'KeyTech', 800, 349.00, 30, 1, '87键机械键盘，Cherry红轴', 'https://picsum.photos/400/400?random=4', 'Cherry红轴 | RGB背光'),
('办公笔记本套装', 'SKU-005', '办公用品', 'MoleSkin', 150, 59.00, 80, 1, 'A5精装笔记本+签字笔套装', 'https://picsum.photos/400/400?random=5', 'A5精装 | 含签字笔');
