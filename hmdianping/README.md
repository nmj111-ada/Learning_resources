# 黑马点评学习项目

基于黑马程序员「黑马点评」课程的 Spring Boot 后端练习，记录 Redis 登录、缓存与秒杀下单的学习进度。当前为学习中的代码快照，不代表完整业务已验收。

## 技术环境

- Java 17 或更高版本、Maven 3.6+
- Spring Boot 2.7.18、MyBatis-Plus 3.4.3
- MySQL 8、单机 Redis
- Redisson 3.23.0（普通客户端依赖，通过 `RedissonConfig` 注册 Bean）

## 目录

```text
src/main/java/com/hmdp/
  config/        MVC、MyBatis、Redisson 配置
  controller/    HTTP 接口
  service/       业务逻辑
  entity/        数据库实体
  dto/           请求与响应对象
  utils/         拦截器、缓存、锁与用户上下文
src/main/resources/
  application.yaml       环境变量配置
  db/hmdp-schema.sql     11 张表的结构，不含数据
  seckill.lua            秒杀资格校验脚本
src/test/java/           登录流程回归测试
```

## 本地运行

1. 创建 `hmdp` 数据库，导入 `src/main/resources/db/hmdp-schema.sql`。初始化文件不含课程演示数据，需要自行准备店铺、优惠券及 Redis 库存。
2. 启动 MySQL 和单机 Redis，在终端或 IDE 的运行配置中设置环境变量：

| 变量 | 默认值 / 用途 |
| --- | --- |
| `DB_URL` | 本机 MySQL 的 `hmdp` 数据库，可覆盖完整 JDBC URL |
| `DB_USERNAME` / `DB_PASSWORD` | `root` / 空，请按本机配置设置 |
| `REDIS_HOST` / `REDIS_PORT` | `localhost` / `6379` |
| `REDIS_DATABASE` | `0` |
| `REDIS_USERNAME` / `REDIS_PASSWORD` | 空；启用 ACL 或密码时设置 |
| `REDIS_SSL` | `false` |
| `SERVER_PORT` | `8081` |
| `IMAGE_UPLOAD_DIR` | `./uploads/`；请提前创建可写目录，路径以 `/` 结尾 |
| `APP_LOG_LEVEL` | `info` |

3. 在本文件夹执行：

```shell
mvn test
mvn spring-boot:run
```

环境变量由 Spring Boot 读取，不会自动加载 `.env` 文件。前端、Nginx 与图片资源需另行准备，本文件夹仅包含后端。

## Redisson 配置说明

`RedissonConfig` 读取 `spring.redis` 的主机、端口、数据库、用户名、密码及 TLS 配置，创建 `RedissonClient` Bean，供订单服务注入。关闭应用时调用 `shutdown()` 释放连接。

使用普通 `redisson` 依赖，避免 starter 默认引入的 Spring Data Redis 3.x 适配器与本项目 Spring Boot 2.7 / Spring Data Redis 2.7 冲突。该配置面向单机 Redis，不适用于 Sentinel 或 Cluster。

## 验证范围与当前限制

登录回归测试覆盖双拦截器顺序、公开接口续期、未登录拒绝访问、ThreadLocal 清理及用户 Hash 字符串序列化。测试不要求连接真实数据库或 Redis。

秒杀下单仍处于课程练习阶段：订单服务中的内联 Lua 使用 `KEYS[1]`，调用处却传入空 key 列表；该内联脚本也尚未接入资源目录中的资格校验脚本。异步订单代理赋值与入队顺序也需要后续完善。当前未完成真实 Redis 锁和完整秒杀链路联调。

## 提交与隐私

仓库不包含真实连接密码、本地配置、IDE 配置、编译产物、日志、上传文件及数据库用户记录。数据库仅提供建表结构；运行配置使用环境变量。请勿提交包含个人数据的 SQL 导出文件或真实密钥。
