# 购物车信息管理系统（Shopping Cart Management System）

一个基于 **Spring Boot 3 + MyBatis + SQLite** 的购物车信息管理系统，包含用户、商品、分类、品牌、购物车、订单、收货地址、收藏、评价等模块，前端使用原生 HTML + jQuery 通过 Ajax 调用后端接口。

项目为课程设计/练习项目，采用「零额外中间件」的设计：数据库使用 SQLite 单文件，启动时自动执行 `schema.sql` 建表与 `data.sql` 初始化演示数据，**克隆下来即可直接运行，无需手动安装配置数据库**。

---

## 一、技术栈

| 层次 | 技术 | 版本 |
| --- | --- | --- |
| 语言 | Java | 17+（本机验证 21 亦可运行） |
| 框架 | Spring Boot | 3.5.14 |
| 持久层 | MyBatis Spring Boot Starter | 3.0.5 |
| 数据库 | SQLite（sqlite-jdbc） | 3.42.0.0 |
| 内置服务器 | Spring Boot 内嵌 Tomcat | 随 Boot 版本 |
| 前端 | HTML5 + CSS3 + jQuery（Ajax） | jQuery 3.7.1 |
| 构建工具 | Maven | 3.6+ |
| AI 能力（可选） | LangChain4j + 阿里云 DashScope（通义千问） | 1.1.0-beta7 |

---

## 二、功能模块

### 1. 用户模块
- 用户注册（校验用户名/密码非空、用户名唯一）
- 用户登录（会话保持，`HttpSession` 存储登录用户）
- 退出登录、获取当前登录用户信息

### 2. 商品与基础数据模块
- **商品管理**：列表查询、按关键字检索、新增、修改、删除、批量删除
- **商品分类管理**：列表、详情、新增、修改、删除、批量删除
- **品牌管理**：列表查询（只读，数据由 SQL 初始化脚本维护）

### 3. 购物车模块（核心）
- 查询当前用户购物车列表
- 添加商品到购物车：校验商品是否存在、库存是否充足
- 同一商品重复添加时自动累加数量，并再次校验累计数量不超过库存
- 修改购买数量、删除单项、批量删除

### 4. 订单模块（核心）
- **订单结算**：一键将购物车结算为订单，`@Transactional` 保证订单头、订单明细、清空购物车三步操作的原子性
- 自动计算订单总价，自动生成订单号（`yyyyMMddHHmmss_用户ID`）
- 结算时自动读取用户默认收货地址写入订单
- 订单列表查询、按关键字检索、订单详情
- 订单状态流转：`待发货 → 已发货 → 已完成`，以及 `已取消`
- 删除订单、批量删除订单
- 订单模型按 **第三范式（3NF）** 设计，拆分为订单头（`orders`）与订单明细（`order_item`）两张表

### 5. 收货地址模块
- 地址列表、新增/修改地址、删除地址
- 支持设置默认地址，供订单结算时取用

### 6. 收藏与评价模块
- 商品收藏、取消收藏、收藏列表
- 商品评价列表、发表评价（含星级）

---

## 三、快速开始

### 环境要求
- JDK 17 或更高版本
- Maven 3.6+

### 1. 克隆项目

```bash
git clone https://github.com/zeze1225/ShopMS.git
cd ShopMS
```

### 2. 运行

方式一，命令行：

```bash
mvn spring-boot:run
```

方式二，打包后运行：

```bash
mvn clean package -DskipTests
java -jar target/shopping-cart-1.0.0.jar
```

方式三，IDEA 中直接运行 `ShoppingCartApplication` 的 `main` 方法。

### 3. 访问

启动成功后浏览器打开：

```
http://localhost:8080/login.html
```

### 4. 演示账号

| 用户名 | 密码 |
| --- | --- |
| `admin` | `123456` |

> 初始化脚本还会写入 3 个分类、3 个品牌、8 件商品、1 条默认收货地址和 2 条收藏记录，方便直接体验完整流程。

---

## 四、数据库设计

数据库文件位于 `data/shopping.db`，首次启动时自动创建。建表脚本见 [`src/main/resources/schema.sql`](src/main/resources/schema.sql)，初始化数据见 [`src/main/resources/data.sql`](src/main/resources/data.sql)。

| # | 表名 | 说明 |
| --- | --- | --- |
| 1 | `users` | 用户表 |
| 2 | `category` | 商品分类表 |
| 3 | `brand` | 品牌表 |
| 4 | `product` | 商品表（关联分类、品牌） |
| 5 | `cart` | 购物车表 |
| 6 | `orders` | 订单头表（订单号、总价、状态、地址、下单时间） |
| 7 | `order_item` | 订单明细表（商品、数量、下单时单价） |
| 8 | `address` | 收货地址表 |
| 9 | `collect` | 商品收藏表 |
| 10 | `comment` | 商品评价表 |
| 11 | `product_img` | 商品图片表 |

设计要点：
- 订单与订单明细拆分，满足 3NF，避免冗余；明细中保存**下单时单价**，保证历史订单金额不受后续改价影响。
- MyBatis 开启 `map-underscore-to-camel-case`，数据库下划线字段自动映射为 Java 驼峰属性，Mapper XML 中无需逐个编写 `resultMap`。

---

## 五、接口一览

统一返回 JSON 结构：`{ "code": 0, "msg": "...", "data": ... }`，其中 `code = 0` 表示成功，`code = 1` 表示业务失败，`code = -1` 表示未登录。

### 用户 `/user`

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/user/login` | 登录 |
| POST | `/user/register` | 注册 |
| GET | `/user/info` | 当前登录用户信息 |
| GET | `/user/logout` | 退出登录 |

### 商品 `/product`

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/product/list` | 商品列表（支持 `keyword` 关键字模糊查询） |
| GET | `/product/get` | 商品详情 |
| POST | `/product/add` | 新增商品 |
| POST | `/product/update` | 修改商品 |
| POST | `/product/delete` | 删除商品 |
| POST | `/product/deleteBatch` | 批量删除商品 |

### 分类 `/category`、品牌 `/brand`

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/category/list` | 分类列表 |
| GET | `/category/get` | 分类详情 |
| POST | `/category/add` | 新增分类 |
| POST | `/category/update` | 修改分类 |
| POST | `/category/delete` | 删除分类 |
| POST | `/category/deleteBatch` | 批量删除分类 |
| GET | `/brand/list` | 品牌列表（只读） |

### 购物车 `/cart`

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/cart/list` | 购物车列表 |
| POST | `/cart/add` | 加入购物车（含库存校验） |
| POST | `/cart/update` | 修改数量 |
| POST | `/cart/delete` | 删除单项 |
| POST | `/cart/deleteBatch` | 批量删除 |

### 订单 `/order`

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/order/list` | 订单列表（支持关键字检索） |
| GET | `/order/get` | 订单详情 |
| POST | `/order/create` | 结算购物车生成订单 |
| POST | `/order/updateStatus` | 修改订单状态 |
| POST | `/order/delete` | 删除订单 |
| POST | `/order/deleteBatch` | 批量删除订单 |

### 地址 `/address`、收藏 `/collect`、评价 `/comment`

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/address/list` | 地址列表 |
| POST | `/address/save` | 新增/修改地址 |
| POST | `/address/delete` | 删除地址 |
| GET | `/collect/list` | 收藏列表 |
| POST | `/collect/add` | 添加收藏 |
| POST | `/collect/remove` | 取消收藏 |
| GET | `/comment/list` | 评价列表 |
| POST | `/comment/add` | 发表评价 |

---

## 六、项目结构

```
shopping-cart
├── pom.xml                        # Maven 构建配置
├── data/
│   └── shopping.db                # SQLite 数据库文件（运行时生成）
└── src
    ├── main
    │   ├── java/com/example/shoppingcart
    │   │   ├── ShoppingCartApplication.java   # 启动类
    │   │   ├── controller/        # 9 个控制器，接收前端请求
    │   │   ├── service/           # 业务逻辑层
    │   │   ├── mapper/            # MyBatis Mapper 接口
    │   │   ├── entity/            # 实体类
    │   │   ├── config/            # WebMvc 配置（编码过滤器、静态资源）
    │   │   ├── exception/         # 自定义业务异常
    │   │   └── utils/             # 工具类
    │   └── resources
    │       ├── application.yml    # 主配置
    │       ├── schema.sql         # 建表脚本
    │       ├── data.sql           # 初始化数据
    │       ├── mapper/*.xml       # MyBatis SQL 映射文件
    │       └── static/            # 前端页面（HTML/CSS/JS）
    │           ├── login.html / register.html
    │           ├── main.html / base.html
    │           ├── product.html / category.html / brand.html
    │           ├── cart.html / order.html
    │           └── address.html / collect.html
    └── test
        └── java/.../ShoppingCartApplicationTests.java
```

---

## 七、主要页面

| 页面 | 路径 | 功能 |
| --- | --- | --- |
| 登录 | `/login.html` | 用户登录 |
| 注册 | `/register.html` | 用户注册 |
| 首页框架 | `/main.html` | 系统主框架与导航 |
| 商品管理 | `/product.html` | 商品增删改查 |
| 分类管理 | `/category.html` | 分类增删改查 |
| 品牌管理 | `/brand.html` | 品牌列表（只读） |
| 购物车 | `/cart.html` | 购物车操作与结算 |
| 订单管理 | `/order.html` | 订单查询与状态管理 |
| 收货地址 | `/address.html` | 地址维护 |
| 我的收藏 | `/collect.html` | 收藏列表与取消收藏 |

---

## 八、配置说明

主配置 [`src/main/resources/application.yml`](src/main/resources/application.yml)：

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:sqlite:./data/shopping.db
    driver-class-name: org.sqlite.JDBC
  sql:
    init:
      mode: always                       # 每次启动执行 schema.sql + data.sql
      schema-locations: classpath:schema.sql
      data-locations: classpath:data.sql

mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.example.shoppingcart.entity
  configuration:
    map-underscore-to-camel-case: true
```

> `data.sql` 中全部使用 `INSERT OR IGNORE`，配合 `CREATE TABLE IF NOT EXISTS`，因此反复启动不会重复插入数据、也不会清空已有数据。

### 关于 AI 助手（可选）
项目预留了 LangChain4j + DashScope 依赖与 `aicodehelper` 包，用于后续接入大模型辅助功能。若需启用，请**自行**在本地创建 `src/main/resources/application-local.yml` 并填入自己的 API Key（该文件已被 `.gitignore` 忽略，不会提交到仓库）：

```yaml
spring:
  ai:
    dashscope:
      chat-model:
        model-name: qwen-plus
        api-key: ${DASHSCOPE_API_KEY:你的APIKey}
```

---

## 九、已知问题与后续改进

作为练习项目，以下内容是当前版本的已知不足，也是后续迭代方向：

**安全方面**
- [ ] 用户密码目前为**明文存储**，应改为 BCrypt 加盐哈希
- [ ] 登录鉴权仅依赖 Session 中是否存在 `loginUser`，未引入 Spring Security / 拦截器统一校验
- [ ] 商品、分类、订单的增删改接口未做角色权限控制，任何登录用户均可调用
- [ ] 收货地址、订单详情等接口未校验数据归属，存在越权访问可能

**功能方面**
- [ ] 购物车数量上限（`MAX_CART_ITEMS = 20`）约束定义了但未启用
- [ ] 收藏功能未做重复收藏判断
- [ ] 品牌管理页仅支持只读，未实现增删改
- [ ] 生成订单时未扣减商品库存
- [ ] 商品/订单列表未做分页，数据量大时前端会变慢
- [ ] 已发货订单允许被删除，业务上不应允许
- [ ] `dto` 包为空，尚未引入 VO/DTO 做视图对象与入参对象的分离

**工程质量方面**
- [ ] 单元测试仅有一个空的上下文加载测试，缺乏业务逻辑测试
- [ ] 各 Controller 中登录校验、统一返回结构存在大量重复代码，可抽取为拦截器或统一响应封装
- [ ] 异常处理以 `e.printStackTrace()` 为主，未使用 `@ControllerAdvice` 全局异常处理器
- [ ] SQLite 的 `datetime('now','localtime')` 在部分版本/时区下取值可能不准确

---

## 十、说明

本项目为学习与课程设计用途，用于演示 Spring Boot + MyBatis 的典型分层开发流程与购物车/订单业务建模，**未针对生产环境做安全加固**，请勿直接用于线上业务。

## License

仅用于学习交流。如需引用请注明出处。
