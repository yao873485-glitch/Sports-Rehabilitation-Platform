# 运动康复医疗管理平台

Sports Rehabilitation Medical Management Platform — 一个全栈 Web 应用，用于运动康复医疗的数字化管理。

## 技术栈

| 层级 | 技术 |
|------|------|
| **后端** | Java 11, Spring Boot 2.7.18, MyBatis-Plus 3.5.3, MySQL 8.0 |
| **前端** | Vue 2.6, Element UI 2.15, Vue Router 3.5, Vuex 3.6 |
| **实时通信** | WebSocket, STOMP |
| **API 文档** | Knife4j 4.1 / Swagger |
| **构建工具** | Maven (后端), Vue CLI (前端) |

## 目录结构

```
├── backend/                    # Java 后端（Spring Boot）
│   ├── src/main/java/          # Java 源代码
│   ├── src/main/resources/     # 配置文件、MyBatis XML
│   ├── pom.xml                 # Maven 构建配置
│   └── .env.example            # 环境变量模板
├── frontend/                   # Vue 前端
│   ├── src/                    # 前端源代码
│   │   ├── api/                # API 请求模块
│   │   ├── views/              # 页面组件
│   │   ├── components/         # 可复用组件
│   │   ├── router/             # 路由配置
│   │   ├── store/              # Vuex 状态管理
│   │   └── utils/              # 工具函数
│   ├── public/                 # 静态资源
│   ├── package.json            # 前端依赖配置
│   └── .env.example            # 前端环境变量模板
├── docs/                       # 项目文档
├── .gitignore                  # Git 忽略规则
└── README.md
```

## 快速开始

### 环境要求

- **JDK** 11+
- **Maven** 3.6+
- **Node.js** 16+
- **MySQL** 8.0
- **Redis** (可选，用于缓存和会话管理)

### 1. 创建数据库

```sql
CREATE DATABASE sports_rehabilitation CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
运行docs.sql文件
```

### 2. 配置环境变量

后端环境变量（参考 `backend/.env.example`）：

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| `DB_HOST` | 数据库地址 | `localhost` |
| `DB_PORT` | 数据库端口 | `3306` |
| `DB_NAME` | 数据库名称 | `sports_rehabilitation` |
| `DB_USERNAME` | 数据库用户名 | `root` |
| `DB_PASSWORD` | 数据库密码 | — |
| `REDIS_PASSWORD` | Redis 密码 | — |
| `JWT_SECRET` | JWT 签名密钥（生产环境务必修改） | — |
| `OSS_ACCESS_KEY_ID` | 阿里云 OSS AK（如需图片上传） | — |
| `OSS_ACCESS_KEY_SECRET` | 阿里云 OSS SK | — |

### 3. 启动后端

```bash
cd backend
mvn spring-boot:run
```

后端运行在 `http://localhost:8082`，API 文档地址：`http://localhost:8082/api/doc.html`

### 4. 启动前端

```bash
cd frontend
npm install
npm run serve
```

前端运行在 `http://localhost:3000`，开发代理自动转发 `/api` 请求到后端。

## 功能模块

- 🏥 **患者档案管理** — 患者信息登记、档案查询
- 📋 **医疗方案管理** — 方案制定、配置与查询
- 📊 **评估管理** — 康复评估执行与记录
- 💊 **运动处方** — 处方开具与执行跟踪
- 📚 **健康教育** — 内容管理、图文/视频库
- 🔄 **随访管理** — 随访计划、项目与入组
- 📅 **排课管理** — 教室与课程安排
- 🏋️ **设备管理** — 康复设备信息维护

## License

[MIT](LICENSE)
