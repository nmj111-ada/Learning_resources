# Tlias 前端练习项目

Vue 3 + Vue Router + Element Plus + Axios + ECharts。沿用原来的 `src/views/api`、`src/views/utils` 目录和各页面的中文功能注释。

## 本地启动

本机源码根目录：`D:\heima\vue\vue-tlias-management\vue-tlias-management`（含 package.json 的内层目录）。GitHub 中以 `vue-tlias-management` 目录保存。

```shell
npm install
npm run dev
```

先启动后端 `tlias-web-management`（默认 8080），再打开终端显示的 Vite 地址。开发环境 `/api` 由项目根目录 `vite.config.js` 代理转发，不需要 Nginx。后端地址不同时，可通过终端环境变量 `API_TARGET` 指定。

数据库连接仅在后端本地的 `application.yaml` 或环境变量中配置，不能提交真实配置。前端请求头统一使用 `token`；登录校验已启用，修改后端代码后需要重启后端。

## 已完成

- 登录、退出、未登录路由拦截、令牌失效回登录页、当前账号修改密码。
- 部门新增、编辑回显、修改、删除及部门存在员工时的提示。
- 员工组合条件分页查询、新增、编辑、工作经历动态增删与回显、单条和批量删除。
- 班级条件分页查询、新增、编辑、删除及班级存在学员时的提示。
- 学员条件分页查询、新增、编辑、单条和批量删除、违纪次数和分数更新。
- 员工职位/性别统计、班级人数/学历统计图，操作日志分页查看。
- 头像上传按当前学习进度保留占位，没有调用 OSS 或其他上传服务；已有头像链接仍可展示。

员工新建时的初始密码沿用后端数据库的默认设置，登录后可在右上角修改密码。

## 构建与联调检查

```shell
npm run build
npx playwright install chromium
```

真实浏览器联调脚本是 `tests/integration.mjs`，会写入及删除测试数据、修改并恢复测试账号密码。必须使用独立测试数据库和测试账号，不要对日常数据库运行。脚本不会创建数据库，需先建立与后端表结构一致的独立库，启动指向该库的后端与 Vite。

PowerShell 中通过环境变量传入测试连接（请自行设置测试账号及密码）：

```powershell
$env:E2E_BASE_URL='http://127.0.0.1:5175'
$env:E2E_USERNAME='<独立库测试账号>'
$env:E2E_PASSWORD=Read-Host '输入独立库测试密码'
$env:E2E_ISOLATED_DATABASE='true'
npm run test:integration
```

2026-11-09：使用独立 MySQL 数据库、真实 Spring Boot 服务、Vite 代理和 Chromium 完成 9 组业务检查；覆盖登录、部门、员工及经历、班级、学员及违纪、统计/日志、删除、改密/退出、401 回登录。验证员工查询不返回密码，浏览器无未捕获异常。测试报告与截图在忽略提交的 `test-results` 中。

后端同时修复 JSON 修改参数接收、工作经历映射及删除 SQL、学员批量删除、学生查询排序、班级统计分组和有学员班级删除保护。后端现有 6 个自动化测试通过。

生产构建目前仍有依赖包体积提示，不影响构建；生产环境需要配置静态资源托管、前端路由回退及 `/api` 反向代理。
