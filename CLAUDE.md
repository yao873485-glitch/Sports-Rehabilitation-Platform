# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**运动康复医疗管理平台 (Sports Rehabilitation Medical Management Platform)**

A full-stack web application for sports rehabilitation medical management built with Spring Boot (backend) and Vue 2 (frontend).

- **Backend**: Java 11, Spring Boot 2.7.18, MyBatis-Plus 3.5.3, MySQL 8.0
- **Frontend**: Vue 2.6.14, Element UI 2.15.13, Vue Router 3.5.4, Vuex 3.6.2
- **API Documentation**: Knife4j 4.1.0 / Swagger
- **Real-time Communication**: WebSocket, STOMP
- **Database**: MySQL 8.0, port 3306, database name `sports_rehabilitation`

## Development Commands

### Backend (Maven)

```bash
# Clean and compile (skip tests)
mvn clean compile -DskipTests

# Full build with tests
mvn clean install

# Run Spring Boot application
mvn spring-boot:run

# Backend runs on port 8082
# API docs: http://localhost:8082/api/doc.html
```

### Frontend (Vue)

```bash
cd frontend

# Install dependencies
npm install

# Run development server (port 8080)
npm run serve

# Build for production
npm run build

# Lint and fix
npm run lint
```

## Architecture Overview

### Backend Structure

```
src/main/java/com/rehabilitation/
├── common/          # Common utilities and base classes (Result, PageResult)
├── config/          # Spring configuration (WebSocket, CORS, MyBatis, etc.)
├── controller/      # REST API endpoints (@RestController)
├── dto/             # Data Transfer Objects
├── entity/          # JPA/MyBatis entities
├── exception/       # Global exception handlers
├── mapper/          # MyBatis mappers (interfaces)
├── service/         # Business logic layer
│   └── impl/        # Service implementations
└── util/            # Utility classes

src/main/resources/
├── mapper/          # MyBatis XML mapping files
└── application.yml  # Application configuration
```

### Frontend Structure

```
frontend/src/
├── api/             # API service modules (axios)
├── assets/          # Static assets (images, styles)
├── components/      # Reusable Vue components
├── layout/          # Layout components (Sidebar, Navbar, AppMain)
├── router/          # Vue Router configuration
├── store/           # Vuex state management
├── styles/          # Global SCSS styles
│   ├── variables.scss  # SCSS variables (sidebar width: 280px, colors)
│   └── sidebar.scss    # Sidebar-specific styles
├── utils/           # Utilities (auth, request interceptors, validation)
└── views/           # Page components
    ├── archive/     # Patient archive management
    ├── scheme/      # Medical scheme management
    ├── followup/    # Follow-up management
    └── ...
```

## Critical Technical Details

### Lombok Issues

**IMPORTANT**: Lombok annotation processing has compilation failures in this project (`java.lang.NoClassDefFoundError: Could not initialize class lombok.javac.Javac`).

**Workaround Strategy**:
- For DTOs that receive JSON from frontend: Add **manual getter/setter methods** instead of relying on `@Data`
- For entities: Use **Java reflection** to get/set field values
- Example pattern in `FollowupServiceImpl.java`:
  ```java
  private <T> T getFieldValue(Object obj, String fieldName) {
      try {
          Field field = obj.getClass().getDeclaredField(fieldName);
          field.setAccessible(true);
          return (T) field.get(obj);
      } catch (Exception e) {
          return null;
      }
  }

  private void setFieldValue(Object obj, String fieldName, Object value) {
      try {
          Field field = obj.getClass().getDeclaredField(fieldName);
          field.setAccessible(true);
          field.set(obj, value);
      } catch (Exception e) {
          throw new RuntimeException("Failed to set field: " + fieldName, e);
      }
  }
  ```

### API Request/Response Pattern

All backend APIs follow this standard response format:
```java
// Success
Result.success(data)  // { code: 200, message: "操作成功", data: {...} }

// Error
Result.error(message) // { code: 500, message: "错误信息", data: null }
```

Frontend axios interceptor automatically handles response unwrapping in `utils/request.js`.

### MyBatis-Plus vs MyBatis XML

- **Simple CRUD**: Use MyBatis-Plus `BaseMapper<T>` methods directly
- **Complex queries**: Write custom XML in `src/main/resources/mapper/`
- **Important**: When using XML mappers with Map parameters, always use `@Param` annotation or switch to entity objects

### Database Conventions

- Table names: snake_case (e.g., `followup_patient_enrollment`)
- Column names: snake_case
- Entity properties: camelCase (auto-mapped by MyBatis)
- Always use `DATETIME` for timestamps (not `TIMESTAMP`)
- Primary keys: `BIGINT AUTO_INCREMENT` or `INT AUTO_INCREMENT`

### Frontend Styling System

**Global Variables** (`styles/variables.scss`):
- `$sideBarWidth: 280px` - Used throughout layout system
- `$menuBg: rgb(0, 21, 41)` - Sidebar background
- `$menuText: rgb(255, 255, 255)` - Menu text color
- Font size: 15.75px (sidebar menu items, reduced from 21px)
- Menu item height: 64px

**Sidebar Configuration**:
- Width: 280px (increased from original 210px)
- Font: SimHei (黑体) with fallback to Microsoft YaHei
- Fixed position on left side
- Main content offset: `margin-left: 280px`
- Top navbar: `width: calc(100% - 280px)`

**Color Theme**:
- Primary button color: `rgb(106, 91, 140)` - Used throughout the application
- Table header text: `rgb(81, 90, 110)`
- Table header background: `rgb(248, 248, 249)`
- Active menu text: `#409eff`
- Male icon: `#409eff`, Female icon: `#f56c6c`
- Pagination active page: `rgb(106, 91, 140)`
- Operation buttons (text type): `rgb(106, 91, 140)`

**Table Styling Conventions**:
- No vertical borders (only horizontal lines)
- Header background: `rgb(248, 248, 249)`
- Header text color: `rgb(81, 90, 110)`
- Content alignment: Center for data columns, left for patient info
- First column left padding: 40px
- Cell padding: 12px 20px
- Operation buttons use custom purple theme

### WebSocket Integration

Real-time features use STOMP over WebSocket:
- Endpoint: `ws://localhost:8082/ws`
- Libraries: `@stomp/stompjs`, `sockjs-client`
- Usage pattern: See chat/video conference components

### Common Patterns

**1. Creating New CRUD Module**:
```
Backend:
1. Create entity in entity/
2. Create mapper interface in mapper/
3. Create XML in resources/mapper/
4. Create service in service/ and impl/
5. Create controller in controller/
6. Add DTO in dto/ if needed

Frontend:
1. Create API methods in api/
2. Create view component in views/
3. Add route in router/
4. Add to sidebar menu (if needed)
```

**2. Handling Navigation**:
- Sidebar menu items defined in `router/index.js`
- Metadata controls menu display: `meta: { title: '菜单名', icon: 'el-icon-xxx' }`
- Hidden routes: `hidden: true`

**3. Data Table Pattern**:
```vue
<el-table :data="tableData" border>
  <el-table-column prop="field" label="Label" />
</el-table>
<pagination
  :total="total"
  :page.sync="queryParams.pageNum"
  :limit.sync="queryParams.pageSize"
  @pagination="getList"
/>
```

## Debugging Tips

**Backend Issues**:
1. Check `mvn spring-boot:run` output for startup errors
2. Verify Controller mappings in startup logs: `Mapped "{[/api/xxx]..."`
3. Access API docs: http://localhost:8082/api/doc.html
4. Check database connection in `application.yml`

**Frontend Issues**:
1. Check browser console for API errors
2. Verify proxy configuration in `vue.config.js` (target: `http://localhost:8082`)
3. Check network tab for actual request/response
4. Vue DevTools for component/Vuex debugging

**Common Errors**:
- `404 on /api/*`: Backend not running or proxy misconfigured
- `Column 'xxx' cannot be null`: Missing field value or Lombok getter failure
- `Invalid bound statement`: MyBatis XML mapper not found or incorrect namespace
- Git index lock: Delete `.git/index.lock` if Git operations hang

## Git Commit Message Format

When creating commits, follow this format:
```
<type>: <subject>

<body>

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude <noreply@anthropic.com>
```

Types: `feat`, `fix`, `refactor`, `docs`, `style`, `test`, `chore`

Always use `git commit -m "$(cat <<'EOF' ... EOF)"` pattern for multi-line messages.

## Special Notes

1. **Never commit**:
   - `.idea/`, `.vscode/`, `target/`, `node_modules/`, `dist/`
   - `.env` files, `credentials.json`, or any files with secrets
   - These are already in `.gitignore`

2. **Code Style**:
   - Backend: Follow standard Java conventions
   - Frontend: Vue 2 Options API (not Composition API)
   - Use Chinese comments for business logic descriptions
   - Use English for technical comments

3. **API Documentation**:
   - All controllers should have `@Tag` and `@Operation` annotations for Knife4j
   - Access docs at http://localhost:8082/api/doc.html after backend startup

4. **Testing**:
   - Backend tests: Place in `src/test/java/`
   - Skip tests during build: `-DskipTests` flag
   - Frontend: Currently no test setup

5. **Component Communication**:
   - Parent → Child: Props
   - Child → Parent: `$emit`
   - Global state: Vuex store
   - Sibling components: Event bus or Vuex
