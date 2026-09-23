-- 用户（密码123456）
INSERT OR IGNORE INTO users (id, username, password, phone, address) VALUES (1, 'admin', '123456', '13800000000', '北京市');

-- 分类
INSERT OR IGNORE INTO category (id, name) VALUES (1, '手机');
INSERT OR IGNORE INTO category (id, name) VALUES (2, '日用');
INSERT OR IGNORE INTO category (id, name) VALUES (3, '服装');

-- 品牌
INSERT OR IGNORE INTO brand (id, name) VALUES (1, '华为');
INSERT OR IGNORE INTO brand (id, name) VALUES (2, '小米');
INSERT OR IGNORE INTO brand (id, name) VALUES (3, '其他');

-- 商品
INSERT OR IGNORE INTO product (id, name, price, stock, category_id, brand_id, description) VALUES (1, '手机A', 3999, 100, 1, 1, '');
INSERT OR IGNORE INTO product (id, name, price, stock, category_id, brand_id, description) VALUES (2, '手机B', 1999, 80, 1, 2, '');
INSERT OR IGNORE INTO product (id, name, price, stock, category_id, brand_id, description) VALUES (3, '耳机', 199, 200, 1, 3, '蓝牙耳机');
INSERT OR IGNORE INTO product (id, name, price, stock, category_id, brand_id, description) VALUES (4, '洗发水', 39.9, 300, 2, 3, '');
INSERT OR IGNORE INTO product (id, name, price, stock, category_id, brand_id, description) VALUES (5, '毛巾', 15, 500, 2, 3, '');
INSERT OR IGNORE INTO product (id, name, price, stock, category_id, brand_id, description) VALUES (6, 'T恤', 59, 150, 3, 3, '白色短袖');
INSERT OR IGNORE INTO product (id, name, price, stock, category_id, brand_id, description) VALUES (7, '裤子', 89, 120, 3, 3, '');
INSERT OR IGNORE INTO product (id, name, price, stock, category_id, brand_id, description) VALUES (8, '水杯', 25, 400, 2, 3, '');

-- 收货地址
INSERT OR IGNORE INTO address (id, user_id, receiver, phone, address, is_default) VALUES (1, 1, '张三', '13800000000', '北京市朝阳区', 1);

-- 收藏
INSERT OR IGNORE INTO collect (id, user_id, product_id) VALUES (1, 1, 1);
INSERT OR IGNORE INTO collect (id, user_id, product_id) VALUES (2, 1, 4);

-- 评价
INSERT OR IGNORE INTO comment (id, user_id, product_id, content, star) VALUES (1, 1, 1, '还不错', 5);
INSERT OR IGNORE INTO comment (id, user_id, product_id, content, star) VALUES (2, 1, 4, '一般般', 3);
