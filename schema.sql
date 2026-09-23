CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    phone TEXT,
    address TEXT
);

-- ==================== 2. 商品分类表（新增模块） ====================
CREATE TABLE IF NOT EXISTS category (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL
);

-- ==================== 3. 品牌表 ====================
CREATE TABLE IF NOT EXISTS brand (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL
);

-- ==================== 4. 商品表 ====================
CREATE TABLE IF NOT EXISTS product (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    price REAL NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0,
    category_id INTEGER,
    brand_id INTEGER,
    description TEXT
);

-- ==================== 5. 购物车表 ====================
CREATE TABLE IF NOT EXISTS cart (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1
);

-- ==================== 6. 订单表（3NF：订单头） ====================
CREATE TABLE IF NOT EXISTS orders (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    order_no TEXT NOT NULL,
    total REAL NOT NULL,
    status TEXT NOT NULL DEFAULT '待发货',
    address TEXT,
    create_time TEXT DEFAULT (datetime('now','localtime'))
);

-- ==================== 7. 订单明细表 ====================
CREATE TABLE IF NOT EXISTS order_item (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    order_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    price REAL NOT NULL
);

-- ==================== 8. 收货地址表 ====================
CREATE TABLE IF NOT EXISTS address (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    receiver TEXT NOT NULL,
    phone TEXT NOT NULL,
    address TEXT NOT NULL,
    is_default INTEGER DEFAULT 0
);

-- ==================== 9. 收藏表 ====================
CREATE TABLE IF NOT EXISTS collect (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    create_time TEXT DEFAULT (datetime('now','localtime'))
);

-- ==================== 10. 评价表 ====================
CREATE TABLE IF NOT EXISTS comment (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    order_id INTEGER,
    content TEXT,
    star INTEGER DEFAULT 5,
    create_time TEXT DEFAULT (datetime('now','localtime'))
);

-- ==================== 11. 商品图片表 ====================
CREATE TABLE IF NOT EXISTS product_img (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    product_id INTEGER NOT NULL,
    img_url TEXT,
    sort_order INTEGER DEFAULT 0
);
