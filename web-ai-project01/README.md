# Tlias Web 练习项目

本目录收录 `web-ai-project01` 中的 `tlias-web-management` 后端练习项目，包含部门管理、员工管理、班级管理、统计报表、分页查询和文件上传等练习代码。

## 目录

- `tlias-web-management/pom.xml`：Maven 项目配置。
- `tlias-web-management/src/main/java`：Java 源码。
- `tlias-web-management/src/main/resources`：MyBatis 映射、日志配置和上传页面。
- `tlias-web-management/src/test`：原有测试代码。

## 本地配置

需要 JDK 17、Maven 和自行准备的 MySQL 数据库。仓库不提供数据库备份、数据记录或真实连接信息。

1. 将 `tlias-web-management/src/main/resources/application.example.yaml` 复制为同目录下的 `application.yaml`。
2. 在本地运行环境中设置 `DB_URL`、`DB_USERNAME` 和 `DB_PASSWORD`。`DB_URL` 需填写完整的 MySQL JDBC URL，也可在被忽略的本地 `application.yaml` 中自行填写配置。
3. 自行准备项目所需的数据库表结构，然后从 IDE 启动 `TliasWebManagementApplication`，或在模块目录运行 `mvn spring-boot:run`。

源码保留原有上传目录和日志目录设置，运行前需自行确认相应目录可用。此处仅归档当前练习进度，未修改业务代码、依赖版本或原有测试，未验证构建及数据库运行。

## 上传整理

- Java、XML、HTML、Maven 和测试文件均保留原始内容。
- 原始 `application.yaml` 未上传，仅提供使用环境变量的配置模板。
- `.idea`、`*.iml`、`target`、日志、数据库文件、备份、密钥及本地上传文件不纳入版本管理。
- 更新项目时请继续检查暂存内容；忽略规则不能替代敏感信息检查。
