# CampusForum API 接口文档

## 基础信息

**Base URL**: `http://localhost:8080`

**统一响应格式**:

所有接口返回数据统一使用 `Result<T>` 包装：

```json
{
  "code": "200",
  "message": "成功",
  "data": {}
}
```

**分页响应格式**:

分页接口返回数据使用 `PageResult<T>` 包装：

```json
{
  "total": 100,
  "records": []
}
```

---

## 目录

- [用户管理接口](#用户管理接口)
- [讨论管理接口](#讨论管理接口)
- [回复管理接口](#回复管理接口)
- [收藏管理接口](#收藏管理接口)
- [点赞管理接口](#点赞管理接口)
- [通知管理接口](#通知管理接口)
- [AI聊天接口](#ai聊天接口)
- [通用接口](#通用接口)
- [管理员用户管理接口](#管理员用户管理接口)
- [管理员讨论管理接口](#管理员讨论管理接口)

---

## 用户管理接口

### 1. 用户注册

**接口地址**: `POST /api/users/register`

**接口描述**: 新用户注册账号

**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| username | String | 是 | 用户名 |
| password | String | 是 | 密码 |
| nickname | String | 是 | 昵称 |
| college | String | 是 | 学院 |
| email | String | 是 | 邮箱 |
| bio | String | 否 | 个人简介 |

**请求示例**:

```json
{
  "username": "testuser",
  "password": "123456",
  "nickname": "测试用户",
  "college": "计算机学院",
  "email": "test@example.com",
  "bio": "这是我的个人简介"
}
```

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": {
    "id": 1,
    "username": "testuser",
    "nickname": "测试用户",
    "college": "计算机学院",
    "avatarUrl": null,
    "bio": "这是我的个人简介",
    "role": "user",
    "status": "ACTIVE",
    "createdTime": "2024-01-01T00:00:00"
  }
}
```

---

### 2. 用户登录

**接口地址**: `POST /api/users/login`

**接口描述**: 用户通过用户名和密码进行登录

**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| username | String | 是 | 用户名 |
| password | String | 是 | 密码 |

**请求示例**:

```json
{
  "username": "testuser",
  "password": "123456"
}
```

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": {
    "id": 1,
    "authorization": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

---

### 3. 获取用户信息

**接口地址**: `GET /api/users/getUserById/{userId}`

**接口描述**: 根据用户ID获取用户详细信息

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": {
    "id": 1,
    "username": "testuser",
    "email": "test@example.com",
    "nickname": "测试用户",
    "college": "计算机学院",
    "avatarUrl": "http://example.com/avatar.jpg",
    "bio": "这是我的个人简介",
    "phone": "13800138000",
    "role": "user",
    "status": "ACTIVE",
    "createdTime": "2024-01-01T00:00:00",
    "updatedTime": "2024-01-01T00:00:00"
  }
}
```

---

### 4. 获取用户统计信息

**接口地址**: `GET /api/users/getUserStats/{userId}`

**接口描述**: 根据用户ID获取用户统计信息

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": {
    "postCount": 10,
    "replyCount": 50,
    "likeCount": 100,
    "favoriteCount": 20
  }
}
```

---

### 5. 根据用户名获取用户信息

**接口地址**: `GET /api/users/getUserByUsername/{username}`

**接口描述**: 根据用户名获取用户详细信息

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| username | String | 是 | 用户名 |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": {
    "id": 1,
    "username": "testuser",
    "email": "test@example.com",
    "nickname": "测试用户",
    "college": "计算机学院",
    "avatarUrl": "http://example.com/avatar.jpg",
    "bio": "这是我的个人简介",
    "phone": "13800138000",
    "role": "user",
    "status": "ACTIVE"
  }
}
```

---

### 6. 更新用户信息

**接口地址**: `PUT /api/users`

**接口描述**: 更新用户的基本信息

**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| nickname | String | 否 | 昵称 |
| college | String | 否 | 学院 |
| avatarUrl | String | 否 | 头像URL |
| phone | String | 否 | 手机号 |
| bio | String | 否 | 个人简介 |

**请求示例**:

```json
{
  "userId": 1,
  "nickname": "新昵称",
  "college": "新学院",
  "avatarUrl": "http://example.com/new-avatar.jpg",
  "phone": "13900139000",
  "bio": "新的个人简介"
}
```

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": "用户信息更新成功"
}
```

---

### 7. 修改密码

**接口地址**: `PUT /api/users/change-password`

**接口描述**: 修改用户密码

**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| oldPassword | String | 是 | 旧密码 |
| newPassword | String | 是 | 新密码 |

**请求示例**:

```json
{
  "userId": 1,
  "oldPassword": "123456",
  "newPassword": "654321"
}
```

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": "密码修改成功"
}
```

---

### 8. 注销用户

**接口地址**: `DELETE /api/users/delete/{userId}`

**接口描述**: 根据用户ID注销用户

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": "用户注销成功"
}
```

---

### 9. 用户登出

**接口地址**: `POST /api/users/logout`

**接口描述**: 用户登出系统

**请求头**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| Authorization | String | 是 | JWT Token |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": "用户登出成功"
}
```

---

### 10. 获取登录统计

**接口地址**: `GET /api/users/stats`

**接口描述**: 获取在线用户数和今日登录次数

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": {
    "onlineUsers": 100,
    "todayLogins": 50
  }
}
```

---

## 讨论管理接口

### 1. 发布讨论

**接口地址**: `POST /api/discussions`

**接口描述**: 创建新的讨论帖

**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| title | String | 是 | 讨论标题 |
| content | String | 是 | 讨论内容 |
| annexUrl | String | 否 | 附件URL |
| category | String | 是 | 讨论分类 |
| tags | String | 否 | 标签，逗号分隔 |

**分类说明**:
- `learnAndCommunicate` - 学习交流
- `campusLife` - 校园生活
- `JobHuntingAndEmployment` - 求职就业
- `ClubActivities` - 社团活动

**请求示例**:

```json
{
  "title": "关于Java学习的问题",
  "content": "我想请教一下大家Java的学习路径",
  "annexUrl": "http://example.com/file.pdf",
  "category": "learnAndCommunicate",
  "tags": "Java,学习,编程"
}
```

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": {
    "discussionId": 1,
    "message": "发布成功"
  }
}
```

---

### 2. 删除讨论

**接口地址**: `DELETE /api/discussions/{discussionId}`

**接口描述**: 根据ID删除讨论帖

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| discussionId | Long | 是 | 讨论ID |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": "删除成功"
}
```

---

### 3. 更新讨论

**接口地址**: `PUT /api/discussions`

**接口描述**: 修改讨论帖内容

**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| id | Long | 是 | 讨论ID |
| title | String | 否 | 讨论标题 |
| content | String | 否 | 讨论内容 |
| annexUrl | String | 否 | 附件URL |
| category | String | 否 | 讨论分类 |
| tags | String | 否 | 标签，逗号分隔 |

**请求示例**:

```json
{
  "id": 1,
  "title": "更新后的标题",
  "content": "更新后的内容",
  "category": "campusLife",
  "tags": "校园,生活"
}
```

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": "更新成功"
}
```

---

### 4. 获取最新讨论列表

**接口地址**: `GET /api/discussions`

**接口描述**: 分页获取最新讨论帖列表，支持搜索和筛选

**查询参数**:

| 参数名 | 类型 | 必填 | 默认值 | 描述 |
|--------|------|------|--------|------|
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页数量 |
| category | String | 否 | all | 分类 |
| tags | String | 否 | 标签 |
| keyword | String | 否 | 搜索关键词 |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": {
    "total": 100,
    "records": [
      {
        "id": 1,
        "title": "讨论标题",
        "content": "讨论内容",
        "annexUrl": "http://example.com/file.pdf",
        "userId": 1,
        "category": "learnAndCommunicate",
        "tags": "Java,学习",
        "status": "active",
        "viewCount": 100,
        "replyCount": 10,
        "likeCount": 20,
        "favoriteCount": 5,
        "createdTime": "2024-01-01T00:00:00",
        "updatedTime": "2024-01-01T00:00:00"
      }
    ]
  }
}
```

---

### 5. 获取热门讨论列表

**接口地址**: `GET /api/discussions/hot`

**接口描述**: 分页获取热门讨论帖列表（按浏览量和回复数排序）

**查询参数**:

| 参数名 | 类型 | 必填 | 默认值 | 描述 |
|--------|------|------|--------|------|
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页数量 |
| category | String | 否 | all | 分类 |
| tags | String | 否 | 标签 |
| keyword | String | 否 | 搜索关键词 |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": {
    "total": 50,
    "records": [
      {
        "id": 1,
        "title": "热门讨论标题",
        "content": "讨论内容",
        "viewCount": 1000,
        "replyCount": 100,
        "likeCount": 200,
        "favoriteCount": 50
      }
    ]
  }
}
```

---

### 6. 获取讨论详情

**接口地址**: `GET /api/discussions/{discussionId}`

**接口描述**: 根据ID获取讨论帖详情，包含回复列表

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| discussionId | Long | 是 | 讨论ID |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": {
    "title": "讨论标题",
    "content": "讨论内容",
    "annexUrl": "http://example.com/file.pdf",
    "userId": 1,
    "category": "learnAndCommunicate",
    "tags": "Java,学习",
    "status": "active",
    "viewCount": 100,
    "replyCount": 10,
    "likeCount": 20,
    "favoriteCount": 5,
    "replies": [
      {
        "id": 1,
        "content": "回复内容",
        "userId": 2,
        "discussionId": 1,
        "replyLayer": 0,
        "parentReplyId": null,
        "status": "active",
        "likeCount": 5,
        "createdTime": "2024-01-01T00:00:00",
        "updatedTime": "2024-01-01T00:00:00"
      }
    ],
    "createdTime": "2024-01-01T00:00:00",
    "updatedTime": "2024-01-01T00:00:00"
  }
}
```

---

### 7. 获取热门标题

**接口地址**: `GET /api/discussions/hotTitles`

**接口描述**: 获取热门讨论标题列表

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": {
    "total": 50,
    "records": [
      {
        "discussionId": 1,
        "title": "热门讨论标题1"
      },
      {
        "discussionId": 2,
        "title": "热门讨论标题2"
      }
    ]
  }
}
```

---

### 8. 获取用户讨论

**接口地址**: `GET /api/discussions/user/{userId}`

**接口描述**: 获取指定用户发布的所有讨论

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |

**查询参数**:

| 参数名 | 类型 | 必填 | 默认值 | 描述 |
|--------|------|------|--------|------|
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页数量 |
| category | String | 否 | all | 分类 |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": {
    "total": 20,
    "records": [
      {
        "id": 1,
        "title": "用户讨论标题",
        "content": "讨论内容",
        "userId": 1,
        "createdTime": "2024-01-01T00:00:00"
      }
    ]
  }
}
```

---

## 回复管理接口

### 1. 添加回复

**接口地址**: `POST /api/replies`

**接口描述**: 添加新的回复

**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| discussionId | Long | 是 | 讨论ID |
| parentReplyId | Long | 否 | 父回复ID（可选） |
| content | String | 是 | 回复内容 |
| replyLayer | Integer | 否 | 回复层级（可选，如果不提供则自动计算） |

**请求示例**:

```json
{
  "discussionId": 1,
  "parentReplyId": null,
  "content": "这是我的回复",
  "replyLayer": 0
}
```

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": {
    "replyId": 1,
    "message": "回复成功"
  }
}
```

---

### 2. 删除回复

**接口地址**: `DELETE /api/replies/{replyId}`

**接口描述**: 删除回复（软删除）

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| replyId | Long | 是 | 回复ID |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": "删除成功"
}
```

---

### 3. 更新回复

**接口地址**: `PUT /api/replies`

**接口描述**: 修改回复内容

**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| id | Long | 是 | 回复ID |
| content | String | 是 | 回复内容 |

**请求示例**:

```json
{
  "id": 1,
  "content": "更新后的回复内容"
}
```

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": "更新成功"
}
```

---

### 4. 隐藏回复

**接口地址**: `POST /api/replies/{replyId}/hide`

**接口描述**: 隐藏回复内容

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| replyId | Long | 是 | 回复ID |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": "回复已隐藏"
}
```

---

### 5. 显示回复

**接口地址**: `POST /api/replies/{replyId}/show`

**接口描述**: 重新显示已隐藏的回复

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| replyId | Long | 是 | 回复ID |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": "回复已显示"
}
```

---

### 6. 获取讨论回复

**接口地址**: `GET /api/replies/discussion`

**接口描述**: 获取指定讨论的所有回复

**查询参数**:

| 参数名 | 类型 | 必填 | 默认值 | 描述 |
|--------|------|------|--------|------|
| discussionId | Long | 是 | - | 讨论ID |
| replyLayer | Integer | 否 | - | 回复层级（可选） |
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页数量 |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": {
    "total": 50,
    "records": [
      {
        "id": 1,
        "content": "回复内容",
        "userId": 2,
        "discussionId": 1,
        "replyLayer": 0,
        "parentReplyId": null,
        "status": "active",
        "likeCount": 5,
        "createdTime": "2024-01-01T00:00:00",
        "updatedTime": "2024-01-01T00:00:00"
      }
    ]
  }
}
```

---

### 7. 获取子回复

**接口地址**: `GET /api/replies/{replyId}/children`

**接口描述**: 获取指定回复的所有子回复

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| replyId | Long | 是 | 回复ID |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": [
    {
      "id": 2,
      "content": "子回复内容",
      "userId": 3,
      "discussionId": 1,
      "replyLayer": 1,
      "parentReplyId": 1,
      "status": "active",
      "likeCount": 2,
      "createdTime": "2024-01-01T00:00:00"
    }
  ]
}
```

---

### 8. 获取用户回复

**接口地址**: `GET /api/replies/user/{userId}`

**接口描述**: 获取指定用户的所有回复

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |

**查询参数**:

| 参数名 | 类型 | 必填 | 默认值 | 描述 |
|--------|------|------|--------|------|
| discussionId | Long | 否 | - | 讨论ID |
| replyLayer | Integer | 否 | - | 回复层级 |
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页数量 |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": {
    "total": 30,
    "records": [
      {
        "id": 1,
        "content": "用户回复内容",
        "userId": 1,
        "discussionId": 1,
        "createdTime": "2024-01-01T00:00:00"
      }
    ]
  }
}
```

---

### 9. 热门回复

**接口地址**: `GET /api/replies/top`

**接口描述**: 获取热门回复（按点赞数或回复数）

**查询参数**:

| 参数名 | 类型 | 必填 | 默认值 | 描述 |
|--------|------|------|--------|------|
| limit | Integer | 否 | 10 | 数量 |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": [
    {
      "id": 1,
      "content": "热门回复内容",
      "likeCount": 100,
      "createdTime": "2024-01-01T00:00:00"
    }
  ]
}
```

---

## 收藏管理接口

### 1. 收藏/取消收藏

**接口地址**: `POST /api/favorites`

**接口描述**: 对讨论进行收藏或取消收藏

**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| discussionId | Long | 是 | 讨论ID |

**请求示例**:

```json
{
  "userId": 1,
  "discussionId": 1
}
```

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": {
    "status": 1,
    "favoriteCount": 100,
    "message": "收藏成功"
  }
}
```

**响应字段说明**:
- `status`: 0-取消收藏, 1-收藏
- `favoriteCount`: 帖子收藏数
- `message`: 操作结果消息

---

### 2. 检查是否已收藏

**接口地址**: `GET /api/favorites/check`

**接口描述**: 检查用户是否已收藏该讨论

**查询参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| discussionId | Long | 是 | 讨论ID |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": true
}
```

---

### 3. 获取用户收藏列表

**接口地址**: `GET /api/favorites/user`

**接口描述**: 获取用户收藏的讨论列表

**查询参数**:

| 参数名 | 类型 | 必填 | 默认值 | 描述 |
|--------|------|------|--------|------|
| userId | Long | 是 | - | 用户ID |
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页数量 |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": {
    "total": 20,
    "records": [
      {
        "id": 1,
        "userId": 1,
        "discussionId": 1,
        "status": 1,
        "createdTime": "2024-01-01T00:00:00"
      }
    ]
  }
}
```

---

### 4. 获取收藏数

**接口地址**: `GET /api/favorites/count`

**接口描述**: 获取讨论的收藏数量

**查询参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| discussionId | Long | 是 | 讨论ID |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": 100
}
```

---

## 点赞管理接口

### 1. 点赞/取消点赞

**接口地址**: `POST /api/likes`

**接口描述**: 对讨论或回复进行点赞或取消点赞

**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| targetType | String | 是 | 目标类型（discussion 或 reply） |
| targetId | Long | 是 | 目标ID |

**请求示例**:

```json
{
  "userId": 1,
  "targetType": "discussion",
  "targetId": 1
}
```

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": {
    "isLiked": true,
    "likeCount": 100,
    "message": "点赞成功"
  }
}
```

**响应字段说明**:
- `isLiked`: 是否已点赞
- `likeCount`: 点赞数
- `message`: 操作结果消息

---

### 2. 检查是否已点赞

**接口地址**: `GET /api/likes/check`

**接口描述**: 检查用户是否已对目标点赞

**查询参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| targetType | String | 是 | 目标类型（discussion 或 reply） |
| targetId | Long | 是 | 目标ID |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": true
}
```

---

### 3. 获取点赞数

**接口地址**: `GET /api/likes/count`

**接口描述**: 获取目标的点赞数量

**查询参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| targetType | String | 是 | 目标类型（discussion 或 reply） |
| targetId | Long | 是 | 目标ID |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": 100
}
```

---

### 4. 获取用户点赞列表

**接口地址**: `GET /api/likes/user/discussion`

**接口描述**: 获取用户点赞的讨论列表

**查询参数**:

| 参数名 | 类型 | 必填 | 默认值 | 描述 |
|--------|------|------|--------|------|
| userId | Long | 是 | - | 用户ID |
| targetType | String | 否 | discussion | 目标类型 |
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页数量 |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": {
    "total": 50,
    "records": [
      {
        "id": 1,
        "userId": 1,
        "targetType": "discussion",
        "targetId": 1,
        "status": 1,
        "createdTime": "2024-01-01T00:00:00"
      }
    ]
  }
}
```

---

## 通知管理接口

### 1. 获取通知列表

**接口地址**: `GET /api/notifications`

**接口描述**: 获取当前用户的通知列表

**查询参数**:

| 参数名 | 类型 | 必填 | 默认值 | 描述 |
|--------|------|------|--------|------|
| userId | Long | 是 | - | 用户ID |
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页数量 |
| type | String | 否 | - | 通知类型：all, like, favorite, reply, system |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": {
    "total": 50,
    "records": [
      {
        "id": 1,
        "userId": 1,
        "type": "like",
        "relatedId": 1,
        "senderId": 2,
        "title": "点赞通知",
        "content": "用户A点赞了你的讨论",
        "isRead": false,
        "createdTime": "2024-01-01T00:00:00"
      }
    ]
  }
}
```

**通知类型说明**:
- `like` - 点赞通知
- `reply` - 回复通知
- `favorite` - 收藏通知
- `system` - 系统通知

---

### 2. 获取未读通知数量

**接口地址**: `GET /api/notifications/unread/count`

**接口描述**: 获取当前用户的未读通知数量

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": 10
}
```

---

### 3. 标记通知为已读

**接口地址**: `PUT /api/notifications/{id}/read`

**接口描述**: 将指定通知标记为已读

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| id | Long | 是 | 通知ID |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": "标记已读成功"
}
```

---

### 4. 标记所有通知为已读

**接口地址**: `PUT /api/notifications/read/all`

**接口描述**: 将当前用户的所有通知标记为已读

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": "全部标记已读成功"
}
```

---

## AI聊天接口

### 1. AI对话

**接口地址**: `GET /api/ai/chat/{userInput}/{duty}/{userId}/{sessionId}`

**接口描述**: 与AI学习助手进行对话

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| userInput | String | 是 | 用户输入的消息 |
| duty | String | 是 | AI角色职责 |
| userId | String | 是 | 用户ID |
| sessionId | String | 是 | 会话ID |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": {
    "content": "这是AI的回复内容",
    "status": "success",
    "errorMsg": null,
    "timestamp": 1704067200000,
    "token": 100,
    "responseTime": 500
  }
}
```

---

### 2. AI流式对话

**接口地址**: `GET /api/ai/chatStream/{userInput}/{duty}`

**接口描述**: 与AI学习助手进行流式对话

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| userInput | String | 是 | 用户输入的消息 |
| duty | String | 是 | AI角色职责 |

**响应类型**: `text/event-stream`

**响应示例**:

```
data: 这是AI的
data: 回复内容
data: 
```

---

### 3. AI图片生成

**接口地址**: `GET /api/ai/image/{description}`

**接口描述**: 与AI学习助手进行图片生成

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| description | String | 是 | 图片生成描述 |

**响应示例**:

```
http://example.com/generated-image.jpg
```

---

## 通用接口

### 1. 文件上传

**接口地址**: `POST /api/common/upload`

**接口描述**: 上传文件到阿里云OSS存储，支持图片、文档等各种文件类型

**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| file | MultipartFile | 是 | 上传的文件 |

**请求类型**: `multipart/form-data`

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": "http://example.com/uploads/uuid-filename.jpg"
}
```

---

## 管理员用户管理接口

### 1. 获取用户列表

**接口地址**: `GET /api/admin/users`

**接口描述**: 分页获取所有用户信息

**查询参数**:

| 参数名 | 类型 | 必填 | 默认值 | 描述 |
|--------|------|------|--------|------|
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页数量 |
| username | String | 否 | - | 用户名搜索 |
| status | String | 否 | - | 用户状态 |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": {
    "total": 100,
    "records": [
      {
        "id": 1,
        "username": "testuser",
        "email": "test@example.com",
        "nickname": "测试用户",
        "role": "user",
        "status": "ACTIVE",
        "createdTime": "2024-01-01T00:00:00"
      }
    ]
  }
}
```

---

### 2. 获取用户详情

**接口地址**: `GET /api/admin/users/{userId}`

**接口描述**: 根据用户ID获取详细信息

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": {
    "id": 1,
    "username": "testuser",
    "email": "test@example.com",
    "nickname": "测试用户",
    "college": "计算机学院",
    "avatarUrl": "http://example.com/avatar.jpg",
    "bio": "个人简介",
    "phone": "13800138000",
    "role": "user",
    "status": "ACTIVE",
    "createdTime": "2024-01-01T00:00:00",
    "updatedTime": "2024-01-01T00:00:00"
  }
}
```

---

### 3. 管理员更新用户信息

**接口地址**: `PUT /api/admin/users`

**接口描述**: 管理员更新用户的基本信息

**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| nickname | String | 否 | 昵称 |
| college | String | 否 | 学院 |
| avatarUrl | String | 否 | 头像URL |
| phone | String | 否 | 手机号 |
| bio | String | 否 | 个人简介 |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": "用户信息更新成功"
}
```

---

### 4. 停用用户

**接口地址**: `POST /api/admin/users/{userId}/deactivate`

**接口描述**: 管理员停用用户账号

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": "用户已停用"
}
```

---

### 5. 激活用户

**接口地址**: `POST /api/admin/users/{userId}/activate`

**接口描述**: 管理员激活用户账号

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": "用户已激活"
}
```

---

### 6. 封禁用户

**接口地址**: `POST /api/admin/users/{userId}/ban`

**接口描述**: 管理员封禁用户账号

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": "用户已封禁"
}
```

---

### 7. 删除用户

**接口地址**: `DELETE /api/admin/users/{userId}`

**接口描述**: 管理员删除用户账号（软删除）

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": "用户已删除"
}
```

---

### 8. 用户统计

**接口地址**: `GET /api/admin/users/stats`

**接口描述**: 获取用户统计数据

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": {
    "totalUsers": 1000,
    "totalActiveUsers": 800,
    "totalInactiveUsers": 150,
    "totalBannedUsers": 50
  }
}
```

---

## 管理员讨论管理接口

### 1. 删除讨论

**接口地址**: `DELETE /api/admin/discussions/{discussionId}`

**接口描述**: 删除讨论帖（软删除）

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| discussionId | Long | 是 | 讨论ID |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": "删除成功"
}
```

---

### 2. 关闭讨论

**接口地址**: `POST /api/admin/discussions/close/{discussionId}`

**接口描述**: 关闭讨论帖，禁止新回复

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| discussionId | Long | 是 | 讨论ID |

**响应示例**:

```json
{
  "code": "200",
  "message": "成功",
  "data": "讨论已关闭"
}
```

---

## 附录

### 数据实体说明

#### User（用户）

| 字段名 | 类型 | 描述 |
|--------|------|------|
| id | Long | 用户ID，主键，自增 |
| username | String | 用户名，唯一 |
| password | String | 密码（加密存储） |
| email | String | 邮箱，唯一 |
| nickname | String | 昵称 |
| college | String | 学院 |
| avatarUrl | String | 头像URL |
| bio | String | 个人简介 |
| phone | String | 手机号，唯一 |
| role | String | 用户角色：user-普通用户, admin-管理员 |
| status | String | 用户状态：ACTIVE-正常, INACTIVE-禁用 |
| createdTime | LocalDateTime | 创建时间 |
| updatedTime | LocalDateTime | 更新时间 |

#### Discussion（讨论）

| 字段名 | 类型 | 描述 |
|--------|------|------|
| id | Long | 讨论ID，主键，自增 |
| title | String | 讨论标题 |
| content | String | 讨论内容 |
| annexUrl | String | 附件URL，逗号分隔 |
| userId | Long | 发布者ID，外键关联users表 |
| category | String | 讨论分类：learnAndCommunicate-学习交流, campusLife-校园生活, JobHuntingAndEmployment-求职就业, ClubActivities-社团活动 |
| tags | String | 标签，逗号分隔 |
| status | String | 讨论状态：active-活跃, closed-隐藏, deleted-已删除 |
| viewCount | Integer | 浏览量 |
| replyCount | Integer | 回复数量 |
| likeCount | Integer | 点赞数量 |
| favoriteCount | Integer | 收藏数量 |
| createdTime | LocalDateTime | 创建时间 |
| updatedTime | LocalDateTime | 更新时间 |

#### Reply（回复）

| 字段名 | 类型 | 描述 |
|--------|------|------|
| id | Long | 回复ID，主键，自增 |
| content | String | 回复内容 |
| userId | Long | 回复者ID，外键关联users表 |
| discussionId | Long | 所属讨论ID，外键关联discussions表 |
| replyLayer | Integer | 回复层级，0表示顶级回复，1表示子回复 |
| parentReplyId | Long | 父级回复ID，用于嵌套回复，为null表示顶级回复 |
| status | String | 回复状态：active-活跃, hidden-隐藏, deleted-已删除 |
| likeCount | Integer | 点赞数量 |
| createdTime | LocalDateTime | 创建时间 |
| updatedTime | LocalDateTime | 更新时间 |

#### Favorite（收藏）

| 字段名 | 类型 | 描述 |
|--------|------|------|
| id | Long | 收藏ID，主键，自增 |
| userId | Long | 用户ID |
| discussionId | Long | 讨论ID |
| status | Integer | 1-已收藏, 0-已取消 |
| createdTime | LocalDateTime | 创建时间 |

#### Like（点赞）

| 字段名 | 类型 | 描述 |
|--------|------|------|
| id | Long | 点赞ID，主键，自增 |
| userId | Long | 用户ID |
| targetType | String | 目标类型：discussion 或 reply |
| targetId | Long | 目标ID |
| status | Integer | 1-已点赞, 0-已取消 |
| createdTime | LocalDateTime | 创建时间 |

#### Notification（通知）

| 字段名 | 类型 | 描述 |
|--------|------|------|
| id | Long | 通知ID，主键，自增 |
| userId | Long | 接收通知的用户ID |
| type | String | 通知类型：like-点赞, reply-回复, favorite-收藏, system-系统通知 |
| relatedId | Long | 相关内容ID（帖子ID、回复ID等） |
| senderId | Long | 发送者ID（如果是用户发送的通知） |
| title | String | 通知标题 |
| content | String | 通知内容 |
| isRead | Boolean | 是否已读 |
| createdTime | LocalDateTime | 创建时间 |

### 错误码说明

| 错误码 | 描述 |
|--------|------|
| 200 | 成功 |
| 500 | 服务器内部错误 |

### 认证说明

大部分接口需要在请求头中携带JWT Token进行认证：

```
Authorization: Bearer {token}
```

获取Token的方式：调用用户登录接口 `/api/users/login`，成功后会在响应中返回 `authorization` 字段。

---

**文档版本**: 2.0  
**最后更新**: 2026-04-28  
**更新说明**: 基于实际代码完整扫描，修正了所有与代码不一致的内容