# AGENTS.md

## 项目简介

本项目是基于 RuoYi-Vue 3.9.2 的前后端分离管理系统，适合作为后台管理、权限认证、代码生成、定时任务和业务模块开发的基础框架。

- 后端：Spring Boot 4.0.3、Spring Security、JWT、MyBatis、Druid、Redis、Quartz。
- 前端：Vue 2.6、Vue CLI 4、Vue Router 3、Vuex、Element UI。
- 构建方式：根目录是 Maven 多模块工程，前端位于 `ruoyi-ui`。
- 默认后端端口：`8080`。
- 默认前端开发端口：`80`，接口通过 `/dev-api` 代理到 `http://localhost:8080`。

主要模块：

- `ruoyi-admin`：后端启动模块和 Web 层入口。
- `ruoyi-framework`：框架配置、安全、拦截器等基础能力。
- `ruoyi-system`：系统管理业务模块。
- `ruoyi-quartz`：定时任务模块。
- `ruoyi-generator`：代码生成模块。
- `ruoyi-common`：通用工具、常量、注解和基础类。
- `ruoyi-ui`：Vue 前端项目。
- `sql`：数据库初始化脚本。

## 构建与启动步骤

### 后端

1. 准备 JDK 17、Maven、MySQL 和 Redis。
2. 创建 MySQL 数据库，默认库名为 `ry-vue`。
3. 按需导入初始化脚本：
   - `sql/ry_20260417.sql`
   - `sql/quartz.sql`
4. 检查数据库配置：
   - `ruoyi-admin/src/main/resources/application-druid.yml`
   - 默认连接：`jdbc:mysql://localhost:3306/ry-vue`
   - 默认用户：`root`
   - 默认密码：`password`
5. 检查 Redis 配置：
   - `ruoyi-admin/src/main/resources/application.yml`
   - 默认地址：`localhost:6379`
6. 在项目根目录构建：

```bash
mvn clean package
```

7. 本地开发可直接运行 `ruoyi-admin` 模块中的 Spring Boot 启动类；打包后也可运行生成的 `ruoyi-admin.jar`。

Linux 服务脚本：

```bash
./ry.sh start
./ry.sh stop
./ry.sh restart
./ry.sh status
```

Windows 服务脚本：

```bat
ry.bat
```

### 前端

进入前端目录：

```bash
cd ruoyi-ui
npm install
npm run dev
```

生产构建：

```bash
npm run build:prod
```

预发布构建：

```bash
npm run build:stage
```

构建产物输出到 `ruoyi-ui/dist`。

## 开发环境说明

- Java 版本以根目录 `pom.xml` 为准：`17`。
- Spring Boot 版本：`4.0.3`。
- Maven 模块由根目录 `pom.xml` 统一管理。
- 前端 Node 要求来自 `ruoyi-ui/package.json`：`node >= 8.9`、`npm >= 3.0.0`。实际开发建议使用能兼容 Vue CLI 4 和 Sass 旧版本的 LTS Node。
- 前端环境变量位于：
  - `ruoyi-ui/.env.development`
  - `ruoyi-ui/.env.production`
  - `ruoyi-ui/.env.staging`
- 前端开发代理配置位于 `ruoyi-ui/vue.config.js`，默认将 `/dev-api` 转发到后端 `http://localhost:8080`。
- 后端上传目录默认配置为 `D:/ruoyi/uploadPath`，见 `ruoyi-admin/src/main/resources/application.yml`。
- API 文档默认启用 Springdoc，路径为 `/swagger-ui.html` 和 `/v3/api-docs`。
- Druid 监控默认路径为 `/druid/*`，默认登录用户为 `ruoyi`，密码为 `123456`。

## 常用工具和脚本

后端常用命令：

```bash
mvn clean package
mvn clean install
mvn test
mvn -pl ruoyi-admin -am package
```

前端常用命令：

```bash
cd ruoyi-ui
npm run dev
npm run build:prod
npm run build:stage
npm run preview
```

服务管理脚本：

- `ry.sh`：Linux 下管理 `ruoyi-admin.jar` 的启动、停止、重启和状态。
- `ry.bat`：Windows 下管理 `ruoyi-admin.jar`。

数据库脚本：

- `sql/ry_20260417.sql`：业务和系统表初始化脚本。
- `sql/quartz.sql`：Quartz 定时任务相关表。

## 推荐的代码风格

### 通用

- 使用 UTF-8 编码。
- 优先保持现有 RuoYi 目录结构、命名方式和分层习惯。
- 修改配置时优先更新已有 YAML、Vue CLI 或 Maven 配置，不随意新增平行配置体系。
- 提交前避免包含生成产物、日志、临时文件和本地 IDE 配置。

### 后端 Java

- 包名遵循 `com.ruoyi.*` 结构。
- Controller、Service、Mapper、Domain、XML Mapper 按现有模块分层放置。
- 新增业务优先复用 `ruoyi-common` 中的通用响应、分页、日志、权限和工具类。
- Mapper XML 放在对应 `resources/mapper` 路径下，并与 Mapper 接口保持命名一致。
- 涉及权限、菜单、字典或定时任务时，同步考虑数据库初始化脚本或迁移说明。
- 不在代码中硬编码敏感信息，数据库、Redis、Token 等配置放在配置文件或环境配置中。

### 前端 Vue

- 遵循 `ruoyi-ui/.editorconfig`：空格缩进、缩进宽度 2、LF 换行、UTF-8。
- 页面放在 `ruoyi-ui/src/views`，通用组件放在 `ruoyi-ui/src/components`。
- API 请求模块放在 `ruoyi-ui/src/api`，统一复用现有 request 封装。
- 路由、权限、Vuex 状态管理尽量沿用现有 RuoYi 写法。
- UI 组件优先使用 Element UI，保持后台管理系统的密度和交互一致性。

## 推荐测试与检查命令

后端完整测试：

```bash
mvn test
```

后端打包验证：

```bash
mvn clean package
```

只验证后端启动模块及其依赖：

```bash
mvn -pl ruoyi-admin -am test
mvn -pl ruoyi-admin -am package
```

前端构建检查：

```bash
cd ruoyi-ui
npm run build:prod
```

前端本地运行检查：

```bash
cd ruoyi-ui
npm run dev
```

当前前端 `package.json` 未配置 lint 或 test 脚本；如后续新增 ESLint、单元测试或 E2E 测试，请同步更新本文档中的命令。
