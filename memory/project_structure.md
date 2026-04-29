---
name: 项目结构说明
description: 模块划分、关键文件路径
type: project
---

## 项目整体架构

CampusForum 是一个基于 Spring Boot + MyBatis-Plus 的校园论坛系统，采用前后端分离架构。

## 模块划分

### 1. CampusForum-Commons（公共模块）
- **作用**：存放公共的工具类、配置类、通用响应类
- **关键文件**：
  - `src/main/java/cn/zuo/result/Result.java` - 统一返回结果封装

### 2. CampusForum-Pojo（实体模块）
- **作用**：定义数据库表对应的实体类
- **关键文件**：
  - `src/main/java/cn/zuo/entity/User.java` - 用户实体
  - `src/main/java/cn/zuo/entity/Discussion.java` - 帖子实体
  - `src/main/java/cn/zuo/entity/Reply.java` - 回复实体
  - `src/main/java/cn/zuo/entity/Like.java` - 点赞实体
  - `src/main/java/cn/zuo/entity/Favorite.java` - 收藏实体
  - `src/main/java/cn/zuo/entity/Notification.java` - 通知实体
  - `src/main/java/cn/zuo/entity/Course.java` - 课程实体

### 3. CampusForum-Server（后端服务）
- **作用**：业务逻辑实现、API接口提供
- **关键目录**：

#### Controller层（控制器）
```
src/main/java/cn/zuo/controller/
├── user/                  # 用户相关接口
│   ├── AuthController.java      # 认证
│   ├── NotificationController.java  # 通知
│   ├── UserProfileController.java  # 用户中心
│   ├── DiscussionController.java    # 帖子
│   ├── ReplyController.java       # 回复
│   ├── LikeController.java        # 点赞
│   ├── FavoriteController.java    # 收藏
│   └── AgentController.java      # AI聊天
└── admin/                 # 管理员接口
    └── AdminController.java     # 管理功能
```

#### Service层（业务逻辑）
```
src/main/java/cn/zuo/service/
├── UserService.java          # 用户服务
├── NotificationService.java    # 通知服务
├── DiscussionService.java     # 帖子服务
├── ReplyService.java         # 回复服务
├── LikeService.java          # 点赞服务
├── FavoriteService.java      # 收藏服务
└── impl/
    ├── UserServiceImpl.java
    ├── NotificationServiceImpl.java
    └── ...
```

#### Mapper层（数据访问）
```
src/main/java/cn/zuo/mapper/
├── UserMapper.java
├── NotificationMapper.java
├── DiscussionMapper.java
├── ReplyMapper.java
├── LikeMapper.java
├── FavoriteMapper.java
└── CourseMapper.java
```

### 4. 前端资源
```
src/main/resources/
├── productUI/               # UI页面
│   └── campusForum.html     # 主页面
├── static/
│   └── js/
│       └── api.js          # API封装
└── application.yml         # 配置文件
```

### 5. 数据库脚本
```
db/
└── campusforum.sql          # 数据库初始化脚本
```

## 关键技术点

### 1. 数据库设计
- 使用 MySQL 作为主数据库
- 用户表（users）：存储用户基本信息
- 帖子表（discussions）：存储帖子内容
- 回复表（replies）：存储评论和回复
- 点赞表（likes）：记录用户点赞行为
- 收藏表（favorites）：记录用户收藏行为
- 通知表（notifications）：存储系统通知
- 课程表（courses）：存储课程信息

### 2. API设计规范
- 使用 RESTful 风格
- 统一响应格式：`{ code: 200, message: "success", data: {} }`
- 使用 HTTP 状态码
- 接口路径遵循 REST 约定

### 3. 前端架构
- 单页应用（SPA）模式
- 使用 Tailwind CSS 构建响应式界面
- 组件化思维组织代码
- 使用原生 JavaScript，轻量高效

### 4. 安全考虑
- 密码加密存储（MD5，实际项目应使用更安全的算法）
- 用户权限控制
- 接口访问控制（通过 X-User-Id 头传递用户ID）

## 启动流程

1. 执行数据库初始化脚本
2. 修改 `application.yml` 中的数据库配置
3. 运行 Spring Boot 主类
4. 访问 `http://localhost:8088/productUI/campusForum.html`

## 文件命名规范

### 后端
- 控制器：XXXController
- 服务：XXXService / XXXServiceImpl
- 实体：XXX（使用名词，如 User、Discussion）
- Mapper：XXXMapper

### 前端
- 页面：campusForum.html
- JS文件：api.js、app.js
- CSS类名：使用 BEM 规范

## 目录结构图

```
CampusForum/
├── CampusForum-Commons/      # 公共模块
│   └── src/main/java/cn/zuo/result/
├── CampusForum-Pojo/         # 实体模块
│   └── src/main/java/cn/zuo/entity/
│   └── src/main/java/cn/zuo/mapper/
├── CampusForum-Server/       # 后端服务
│   └── src/main/java/cn/zuo/
│       ├── controller/        # 控制器
│       ├── service/          # 服务层
│       └── utils/            # 工具类
│   └── src/main/resources/
│       ├── productUI/        # 前端UI
│       └── static/           # 静态资源
└── db/                      # 数据库脚本
```

## 扩展建议

1. **微服务架构**：随着业务增长，可以将不同模块拆分为独立服务
2. **消息队列**：使用 RabbitMQ 或 Kafka 处理异步任务
3. **分布式存储**：使用 MinIO 或 AWS S3 存储文件
4. **容器化部署**：使用 Docker 进行部署和管理