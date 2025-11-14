# 问题排查指南

## 当前问题：POST /api/edu-content 返回 404

### 问题原因分析

从错误截图可以看出：
1. GET `/api/edu-content/page` 返回 404 (Not Found)
2. POST `/api/edu-content` 返回 404 (Not Found)

这说明后端Controller没有被正确注册到Spring容器中。

---

## 立即检查清单

### 1️⃣ 确认后端服务是否正常启动

**步骤1**: 打开命令行，执行：
```bash
cd E:\sport_xunlian\data
mvn spring-boot:run
```

**步骤2**: 等待启动完成，查看控制台输出。**必须看到**以下关键日志：

✅ **正确的启动日志**：
```
====================================
运动康复医疗管理平台启动成功!
接口文档地址: http://localhost:8082/api/doc.html
====================================
Started RehabilitationPlatformApplication in X.XXX seconds
```

❌ **如果看到以下错误**：

**错误1: 端口被占用**
```
Port 8082 was already in use
```
**解决**:
```bash
# Windows: 找到并关闭占用8082端口的进程
netstat -ano | findstr :8082
taskkill /PID <进程ID> /F

# 或者修改 src/main/resources/application.yml 中的端口号
```

**错误2: 数据库连接失败**
```
Could not open JDBC Connection for transaction
Access denied for user 'root'@'localhost'
```
**解决**:
1. 确认MySQL服务已启动
2. 检查 `application.yml` 中的数据库配置（第14-16行）
3. 确认数据库 `sports_rehabilitation` 已创建

**错误3: Mapper扫描失败**
```
Invalid bound statement (not found)
```
**解决**: 检查Mapper XML文件是否存在

---

### 2️⃣ 验证Controller是否被注册

**在后端启动日志中搜索**：
```
Mapped "{[/edu-content]
```

如果找不到这行日志，说明Controller没有被Spring扫描到。

**解决方案**：

1. **清理并重新编译**：
```bash
cd E:\sport_xunlian\data
mvn clean install -DskipTests
```

2. **检查Controller类**：
   - 确保有 `@RestController` 注解
   - 确保有 `@RequestMapping` 注解
   - 确保类在 `com.rehabilitation.controller` 包下

---

### 3️⃣ 测试后端接口

**等后端启动成功后**，在浏览器或Postman中测试：

**测试URL**:
```
http://localhost:8082/api/edu-content/page?pageNum=1&pageSize=10
```

**期望响应**（即使数据为空也应该返回200）：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "records": [],
    "total": 0,
    "current": 1,
    "size": 10
  }
}
```

如果返回404，继续下一步。

---

### 4️⃣ 使用API文档测试

访问: `http://localhost:8082/api/doc.html`

在Knife4j界面中：
1. 找到"图文素材库管理"分组
2. 展开"分页查询图文素材"接口
3. 点击"调试"
4. 点击"发送"

如果在API文档中能看到接口，说明Controller已注册。

---

### 5️⃣ 检查数据库表是否存在

**执行SQL**：
```sql
USE sports_rehabilitation;
SHOW TABLES LIKE 'edu_content_repository';
```

**如果表不存在**，执行建表语句（见 sql.md 文件）：

```sql
CREATE TABLE edu_content_repository (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    content_title VARCHAR(255) NOT NULL COMMENT '内容标题',
    content_id VARCHAR(50) NOT NULL UNIQUE COMMENT '内容ID',
    content_type ENUM('文章', '视频') NOT NULL DEFAULT '文章' COMMENT '内容类型',
    category_section VARCHAR(100) NOT NULL COMMENT '所属板块',
    author VARCHAR(100) NOT NULL COMMENT '作者',
    author_type VARCHAR(50) COMMENT '作者类型',
    tags VARCHAR(500) COMMENT '标签',
    content_status ENUM('草稿', '待审核', '已上架', '未上架', '已下架') NOT NULL DEFAULT '草稿' COMMENT '内容状态',
    publish_time DATETIME COMMENT '发布时间',
    support_game_mode TINYINT(1) DEFAULT 1 COMMENT '是否支持游戏模式',
    is_original TINYINT(1) DEFAULT 1 COMMENT '是否原创',
    article_description TEXT COMMENT '文章描述',
    cover_image VARCHAR(500) COMMENT '封面图',
    content_source ENUM('系统录入', '外链') NOT NULL DEFAULT '系统录入' COMMENT '文章来源',
    article_content LONGTEXT COMMENT '文章内容',
    external_link VARCHAR(500) COMMENT '外链地址',
    module_category ENUM('医教', '直数') COMMENT '所属模块',
    view_count INT DEFAULT 0 NOT NULL COMMENT '阅读量',
    like_count INT DEFAULT 0 NOT NULL COMMENT '点赞量',
    view_count_base INT DEFAULT 0 COMMENT '阅读基数',
    like_count_base INT DEFAULT 0 COMMENT '点赞基数',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by VARCHAR(100) COMMENT '创建人',
    updated_by VARCHAR(100) COMMENT '更新人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='宣教图文内容资源库';
```

---

### 6️⃣ 前端配置检查

确认前端代理配置正确（`frontend/vue.config.js`）：

```javascript
proxy: {
  '/api': {
    target: 'http://localhost:8082',  // 确保端口是8082
    changeOrigin: true,
    ws: true,
    pathRewrite: {
      '^/api': '/api'
    }
  }
}
```

**重启前端服务**：
```bash
cd E:\sport_xunlian\data\frontend
npm run serve
```

---

## 最可能的原因

根据错误截图，最可能的原因是：

1. ❌ **后端服务根本没有启动**
2. ❌ **Maven依赖没有正确安装**
3. ❌ **编译时出错，但没有注意到**

## 立即执行的命令

**请按顺序执行以下命令，并将每步的输出结果告诉我**：

### Step 1: 清理并重新编译
```bash
cd E:\sport_xunlian\data
mvn clean
mvn install -DskipTests
```

### Step 2: 启动后端（查看完整日志）
```bash
mvn spring-boot:run
```

**等待看到启动成功的日志，然后截图发给我。**

### Step 3: 在新的命令行窗口测试接口
```bash
curl http://localhost:8082/api/edu-content/page?pageNum=1^&pageSize=10
```

或者在浏览器访问：
```
http://localhost:8082/api/edu-content/page?pageNum=1&pageSize=10
```

---

## 如果上述都正常，但前端还是404

那么问题在前端代理，执行：

```bash
cd E:\sport_xunlian\data\frontend
# 删除node_modules和重新安装
rmdir /s /q node_modules
npm install
npm run serve
```

---

## 需要提供的信息

请提供以下信息以便我进一步诊断：

1. ✅ 后端启动日志（完整的）
2. ✅ 浏览器直接访问 `http://localhost:8082/api/edu-content/page` 的结果
3. ✅ 是否能访问 `http://localhost:8082/api/doc.html`
4. ✅ MySQL是否正常运行
5. ✅ 数据库 `sports_rehabilitation` 是否存在

---

## 快速诊断脚本

创建一个批处理文件 `check.bat`：

```batch
@echo off
echo ===== 检查MySQL服务 =====
sc query MySQL80
echo.

echo ===== 检查8082端口占用 =====
netstat -ano | findstr :8082
echo.

echo ===== 检查数据库连接 =====
mysql -uroot -p1234 -e "USE sports_rehabilitation; SHOW TABLES;"
echo.

pause
```

运行这个脚本可以快速检查环境。
