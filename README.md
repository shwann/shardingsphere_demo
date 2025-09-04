# ShardingSphere 分库分表演示项目

## 项目概述

这是一个基于 Apache ShardingSphere 5.5.2 的分库分表演示项目，展示了如何使用 ShardingSphere JDBC 实现数据库的水平分片功能。项目使用 Spring Boot 2.7.18 框架，集成了 MyBatis 进行数据访问，支持多数据源和分表策略。

## 技术栈

- **Java**: 1.8 以上
- **Spring Boot**: 2.7.18
- **ShardingSphere**: 5.5.2
- **MyBatis**: 3.5.3.1
- **MySQL**: 8.0.33
- **HikariCP**: 4.0.3
- **Lombok**: 用于简化代码
- **Maven**: 项目构建工具

## 项目结构

```
shardingsphere-demo/
├── src/main/java/com/example/
│   ├── ShardingSphereDemoApplication.java    # 主启动类
│   ├── controller/                           # 控制器层
│   │   ├── OrderController.java             # 订单控制器
│   │   └── ShardingTestController.java      # 分片测试控制器
│   ├── entity/                              # 实体类
│   │   └── Order.java                       # 订单实体
│   ├── mapper/                              # 数据访问层
│   │   └── OrderMapper.java                 # 订单Mapper接口
│   └── service/impl/                        # 服务实现层
│       └── OrderServiceImpl.java            # 订单服务实现
├── src/main/resources/
│   ├── application.properties               # 应用配置
│   ├── config.yaml                          # 单数据源分表配置
│   ├── config_1.yaml                        # 多数据源分库分表配置
│   ├── mapper/                              # MyBatis映射文件
│   │   └── OrderMapper.xml                  # 订单SQL映射
│   └── sql/                                 # 数据库初始化脚本
│       ├── demo_ds_0.sql                    # 数据源0初始化脚本
│       └── demo_ds_1.sql                    # 数据源1初始化脚本
└── pom.xml                                  # Maven依赖配置
```

## 分片策略配置

### 1. 单数据源分表配置 (config.yaml)

```yaml
dataSources:
  ds_0:
    dataSourceClassName: com.zaxxer.hikari.HikariDataSource
    driverClassName: com.mysql.jdbc.Driver
    jdbcUrl: jdbc:mysql://localhost:3306/demo_ds_0?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true
    username: root
    password: 12345678
    maxPoolSize: 10

rules:
- !SHARDING
    tables:
      t_order:
        actualDataNodes: ds_0.t_order_$->{0..3}  # 4个分表
        tableStrategy:
          standard:
            shardingColumn: user_id               # 按用户ID分片
            shardingAlgorithmName: t_order_inline
        keyGenerateStrategy:
          column: order_id
          keyGeneratorName: snowflake_generator   # 雪花算法生成ID
      t_order_item:
        actualDataNodes: ds_0.t_order_item_$->{0..3}
        tableStrategy:
          standard:
            shardingColumn: user_id
            shardingAlgorithmName: t_user_item_inline
        keyGenerateStrategy:
          column: order_item_id
          keyGeneratorName: snowflake_generator
    bindingTables:
      - t_order,t_order_item                    # 绑定表，确保关联查询在同一分片

    shardingAlgorithms:
      t_order_inline:
        type: INLINE
        props:
          algorithm-expression: t_order_$->{user_id % 4}  # 取模分片
      t_user_item_inline:
        type: INLINE
        props:
          algorithm-expression: t_order_item_$->{user_id % 4}
    keyGenerators:
      snowflake_generator:
        type: SNOWFLAKE

- !BROADCAST
    tables:
      - t_address                              # 广播表
```

### 2. 多数据源分库分表配置 (config_1.yaml)

```yaml
dataSources:
  ds_0:
    dataSourceClassName: com.zaxxer.hikari.HikariDataSource
    driverClassName: com.mysql.jdbc.Driver
    jdbcUrl: jdbc:mysql://localhost:3306/demo_ds_0?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true
    username: root
    password: 12345678
    maxPoolSize: 10
  ds_1:
    dataSourceClassName: com.zaxxer.hikari.HikariDataSource
    driverClassName: com.mysql.jdbc.Driver
    jdbcUrl: jdbc:mysql://localhost:3306/demo_ds_1?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true
    username: root
    password: 12345678
    maxPoolSize: 10

rules:
  - !SHARDING
    tables:
      t_order:
        actualDataNodes: ds_$->{0..1}.t_order_$->{0..1}  # 2库2表
        tableStrategy:
          standard:
            shardingColumn: order_id                       # 按订单ID分表
            shardingAlgorithmName: t_order_inline
        keyGenerateStrategy:
          column: order_id
          keyGeneratorName: snowflake_generator
      t_order_item:
        actualDataNodes: ds_$->{0..1}.t_order_item_$->{0..1}
        tableStrategy:
          standard:
            shardingColumn: order_id
            shardingAlgorithmName: t_order_item_inline
        keyGenerateStrategy:
          column: order_item_id
          keyGeneratorName: snowflake_generator
    bindingTables:
      - t_order,t_order_item
    defaultDatabaseStrategy:                              # 默认数据库分片策略
      standard:
        shardingColumn: user_id                           # 按用户ID分库
        shardingAlgorithmName: database_inline

    shardingAlgorithms:
      database_inline:
        type: INLINE
        props:
          algorithm-expression: ds_${user_id % 2}         # 按用户ID取模分库
      t_order_inline:
        type: INLINE
        props:
          algorithm-expression: t_order_$->{order_id % 2} # 按订单ID取模分表
      t_order_item_inline:
        type: INLINE
        props:
          algorithm-expression: t_order_item_$->{order_id % 2}
    keyGenerators:
      snowflake_generator:
        type: SNOWFLAKE

  - !BROADCAST
    tables:
      - t_address
```

## 数据库设计

### 表结构

#### 订单表 (t_order)
```sql
CREATE TABLE `t_order` (
  `order_id` bigint NOT NULL AUTO_INCREMENT,
  `order_type` int DEFAULT NULL,
  `user_id` int NOT NULL,
  `address_id` bigint NOT NULL,
  `status` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

#### 订单项表 (t_order_item)
```sql
CREATE TABLE `t_order_item` (
  `order_item_id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL,
  `user_id` int NOT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`order_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

#### 地址表 (t_address) - 广播表
```sql
CREATE TABLE `t_address` (
  `address_id` bigint NOT NULL,
  `address_name` varchar(100) NOT NULL,
  PRIMARY KEY (`address_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

### 分片表命名规则

- **单数据源分表**: `t_order_0`, `t_order_1`, `t_order_2`, `t_order_3`
- **多数据源分库分表**: 
  - 数据库: `demo_ds_0`, `demo_ds_1`
  - 表: `t_order_0`, `t_order_1` (每个库中)

## 核心功能

### 1. 订单管理

#### 创建订单
```http
POST /api/orders/createOrder
Content-Type: application/json

{
  "userId": 123,
  "orderType": 1,
  "addressId": 1001,
  "status": "PENDING"
}
```

#### 查询订单
```http
GET /api/orders/getAllOrders?userId=123
```

#### 删除订单
```http
DELETE /api/orders/{orderId}
```

### 2. 分片测试功能

#### 生成测试数据
```http
POST /api/sharding-test/generate-test-data?count=100
```

#### 测试分片查询
```http
GET /api/sharding-test/test-query-all
```

## 分片策略说明

### 1. 分片键选择

- **单数据源分表**: 使用 `user_id` 作为分片键
- **多数据源分库分表**: 
  - 数据库分片键: `user_id`
  - 表分片键: `order_id`

### 2. 分片算法

- **INLINE 算法**: 使用取模运算进行分片
- **雪花算法**: 用于生成分布式唯一ID

### 3. 绑定表

`t_order` 和 `t_order_item` 设置为绑定表，确保：
- 相同分片键的数据存储在同一分片
- 关联查询性能优化
- 事务一致性保证

### 4. 广播表

`t_address` 设置为广播表，在所有分片中保持数据一致。

## 环境配置

### 1. 数据库准备

创建两个数据库：
```sql
CREATE DATABASE demo_ds_0;
CREATE DATABASE demo_ds_1;
```

### 2. 执行初始化脚本

```bash
# 执行数据源0初始化脚本
mysql -u root -p demo_ds_0 < src/main/resources/sql/demo_ds_0.sql

# 执行数据源1初始化脚本  
mysql -u root -p demo_ds_1 < src/main/resources/sql/demo_ds_1.sql
```

### 3. 修改配置

根据实际环境修改 `application.properties` 和配置文件中的数据库连接信息：

```properties
# 选择使用的配置
spring.datasource.url=jdbc:shardingsphere:classpath:config_1.yaml
```

## 运行项目

### 1. 启动应用

```bash
mvn spring-boot:run
```

### 2. 测试分片功能

```bash
# 生成测试数据
curl -X POST "http://localhost:8080/api/sharding-test/generate-test-data?count=50"

# 查询所有订单
curl "http://localhost:8080/api/sharding-test/test-query-all"

# 创建订单
curl -X POST "http://localhost:8080/api/orders/createOrder" \
  -H "Content-Type: application/json" \
  -d '{"userId": 123, "orderType": 1, "addressId": 1001, "status": "PENDING"}'
```

## 分片效果验证

### 1. 查看分片分布

登录数据库查看数据分布：

```sql
-- 查看各分片表的数据量
SELECT 't_order_0' as table_name, COUNT(*) as count FROM demo_ds_0.t_order_0
UNION ALL
SELECT 't_order_1' as table_name, COUNT(*) as count FROM demo_ds_0.t_order_1
UNION ALL
SELECT 't_order_0' as table_name, COUNT(*) as count FROM demo_ds_1.t_order_0
UNION ALL
SELECT 't_order_1' as table_name, COUNT(*) as count FROM demo_ds_1.t_order_1;
```

### 2. 验证分片规则

```sql
-- 验证用户ID分片规则
SELECT user_id, order_id, 
       CASE WHEN user_id % 2 = 0 THEN 'ds_0' ELSE 'ds_1' END as expected_db,
       CASE WHEN order_id % 2 = 0 THEN 't_order_0' ELSE 't_order_1' END as expected_table
FROM demo_ds_0.t_order_0 LIMIT 10;
```

## 注意事项

1. **分片键选择**: 选择合适的分片键对性能至关重要，应避免数据倾斜
2. **事务处理**: 跨分片事务需要特殊处理，建议使用分布式事务
3. **查询优化**: 尽量使用分片键进行查询，避免全表扫描
4. **数据迁移**: 分片后如需调整分片策略，需要考虑数据迁移方案
5. **监控告警**: 建议添加分片表的监控和告警机制

## 扩展功能

### 1. 读写分离

可以在配置中添加读写分离规则：

```yaml
rules:
- !READWRITE_SPLITTING
  dataSources:
    ds_0:
      writeDataSourceName: ds_0_master
      readDataSourceNames:
        - ds_0_slave1
        - ds_0_slave2
```

### 2. 数据加密

可以添加数据加密规则保护敏感数据：

```yaml
rules:
- !ENCRYPT
  encryptors:
    aes_encryptor:
      type: AES
      props:
        aes-key-value: 123456
  tables:
    t_order:
      columns:
        phone:
          cipherColumn: phone_cipher
          encryptorName: aes_encryptor
```

### 3. 数据脱敏

可以添加数据脱敏规则：

```yaml
rules:
- !MASK
  tables:
    t_order:
      columns:
        phone:
          maskAlgorithm: phone_mask
  maskAlgorithms:
    phone_mask:
      type: MASK_FROM_X_TO_Y
      props:
        from-x: 3
        to-y: 7
        replace-char: '*'
```

## 总结

本项目展示了 ShardingSphere 的核心分片功能，包括：

- 单数据源分表
- 多数据源分库分表  
- 绑定表配置
- 广播表配置
- 雪花算法ID生成
- 分片测试功能

通过这个演示项目，可以深入理解 ShardingSphere 的分片机制和最佳实践，为实际项目中的分库分表方案设计提供参考。
