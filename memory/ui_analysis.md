---
name: UI设计分析
description: campusForum.html 页面分析和需要的后端接口
type: project
---

## UI页面结构分析

### 1. 主要页面（pages）
- **auth**: 登录/注册页面
- **home**: 主页（帖子列表）
- **publish**: 发布帖子页面
- **chat**: AI聊天页面
- **notification**: 通知页面
- **profile**: 个人中心页面
- **admin**: 管理员页面
- **course**: 课程页面

### 2. 主要功能模块

#### 认证功能
- 登录/注册表单切换
- 需要的后端接口：
  - POST /auth/login
  - POST /auth/register
  - GET /auth/userinfo (获取当前用户信息)

#### 主页（帖子列表）
- 帖子展示（标题、内容、分类、作者、时间、点赞、评论数）
- 帖子筛选（分类标签）
- 搜索功能
- 需要的后端接口：
  - GET /discussions (获取帖子列表，支持分页和筛选)
  - GET /discussions/{id} (获取单个帖子详情)
  - POST /discussions (发布帖子)
  - DELETE /api/discussions/{id} (删除帖子)
  - PUT /api/discussions/{id} (更新帖子)

#### 点赞功能
- 帖子点赞/取消点赞
- 评论点赞/取消点赞
- 需要的后端接口：
  - POST /api/like (点赞帖子或评论)
  - DELETE /api/like/{id} (取消点赞)
  - GET /api/like/check (检查点赞状态)

#### 收藏功能
- 收藏/取消收藏帖子
- 需要的后端接口：
  - POST /api/favorite (收藏帖子)
  - DELETE /api/favorite/{id} (取消收藏)
  - GET /api/favorites (获取收藏列表)

#### 评论功能
- 查看帖子评论
- 发表评论
- 需要的后端接口：
  - GET /api/replies (获取帖子评论)
  - POST /api/replies (发表评论)
  - DELETE /api/replies/{id} (删除评论)

#### AI聊天功能
- 消息列表展示
- 发送消息
- AI回复模拟（需要连接真实的AI服务）
- 需要的后端接口：
  - GET /api/chat/history (获取聊天历史)
  - POST /api/chat/message (发送消息)
  - GET /api/chat/ai (获取AI回复，需要对接AI服务)

#### 通知功能
- 通知列表展示
- 标记已读/未读
- 需要的后端接口：
  - GET /api/notifications (获取通知列表)
  - PUT /api/notifications/{id}/read (标记已读)
  - GET /api/notifications/unread/count (获取未读数量)

#### 个人中心
- 我的帖子列表
- 我的回复列表
- 我的收藏列表
- 用户信息编辑
- 需要的后端接口：
  - GET /api/user/profile (获取个人资料)
  - PUT /api/user/profile (更新个人资料)
  - GET /api/user/posts (获取我的帖子)
  - GET /api/user/replies (获取我的回复)
  - GET /api/user/favorites (获取我的收藏)

#### 管理员功能
- 数据看板（用户活跃度、帖子分布等图表）
- 用户管理（查看、封禁、解封）
- 帖子管理（查看、删除）
- 需要的后端接口：
  - GET /api/admin/dashboard (获取管理面板数据)
  - GET /api/admin/users (获取用户列表)
  - PUT /api/admin/users/{id}/status (修改用户状态)
  - GET /api/admin/posts (获取帖子管理列表)
  - DELETE /api/admin/posts/{id} (删除帖子)

#### 课程功能
- 课程表展示
- 课程详情
- 需要的后端接口：
  - GET /api/courses (获取课程列表)
  - GET /api/courses/{id} (获取课程详情)

## 待完善的后端功能

### 已有的控制器
- DiscussionController.java - 帖子相关
- ReplyController.java - 评论相关
- LikeController.java - 点赞相关
- FavoriteController.java - 收藏相关
- AgentController.java - AI聊天相关

### 需要创建的控制器
1. **AuthController** - 认证相关
   - 登录
   - 注册
   - 获取用户信息

2. **NotificationController** - 通知相关
   - 获取通知列表
   - 标记已读
   - 未读数量

3. **UserController** - 用户相关
   - 获取个人信息
   - 更新个人信息
   - 获取用户的帖子、回复、收藏

4. **AdminController** - 管理员相关
   - 获取管理面板数据
   - 用户管理
   - 帖子管理

5. **CourseController** - 课程相关
   - 获取课程列表
   - 获取课程详情

### 需要创建的Service层
- AuthService
- NotificationService
- UserService
- AdminService
- CourseService

### 数据库表
需要检查和完善以下表：
- users（用户表）
- discussions（帖子表）
- replies（回复表）
- likes（点赞表）
- favorites（收藏表）
- notifications（通知表）
- courses（课程表）

### 安全考虑
1. 用户认证和授权
2. SQL注入防护
3. XSS攻击防护
4. 文件上传安全（头像、图片）
5. 敏感信息加密（密码）