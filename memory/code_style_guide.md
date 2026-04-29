# CampusForum 代码风格规范

## Controller 层代码风格

### 1. 类级别注解
```java
@Slf4j
@RestController
@RequestMapping("/模块名")
@Tag(name = "模块名称", description = "模块功能描述")
public class XxxController {
}
```

### 2. 依赖注入
```java
@Resource
private XxxService xxxService;
```

### 3. 方法级别注解
```java
@PostMapping("/路径")
@Operation(summary = "接口简述", description = "接口详细描述")
public Result<返回类型> methodName(@RequestBody XxxDTO xxxDTO) {
}
```

### 4. 参数注解
- 路径参数使用 `@PathVariable`
- 请求体参数使用 `@RequestBody`
- 请求头参数使用 `@RequestHeader`
- 查询参数使用 `@RequestParam`
- Swagger 文档参数使用 `@Parameter(description = "参数说明")`

### 5. 日志记录
```java
log.info("操作描述：{}", 关键参数);
log.error("异常描述", exception);
```

### 6. 返回值规范
- 统一使用 `Result<T>` 包装返回结果
- 成功：`Result.success(data)` 或 `Result.success("操作成功")`
- 失败：`Result.error("错误信息")`

### 7. 参数对象规范
- 接收参数使用 DTO（Data Transfer Object）
- 返回数据使用 VO（View Object）
- DTO 和 VO 分离，职责清晰

### 8. 敏感信息处理
```java
// 不返回密码字段
user.setPassword(null);
```

### 9. 异常处理
```java
try {
    // 业务逻辑
} catch (Exception e) {
    log.error("操作异常", e);
    return Result.error("错误信息");
}
```

### 10. HTTP 方法映射规范
- `@PostMapping` - 创建操作
- `@GetMapping` - 查询操作
- `@PutMapping` - 更新操作
- `@DeleteMapping` - 删除操作

### 11. RESTful 路径设计
- 使用复数形式：`/users`, `/courses`
- 路径变量：`/{id}`, `/{username}`
- 嵌套资源：`/users/{userId}/posts`

### 12. 方法命名规范
- 查询单个：`getXxx`, `getXxxById`, `getXxxByXxx`
- 查询列表：`listXxx`, `searchXxx`
- 创建：`createXxx`, `addXxx`, `registerXxx`
- 更新：`updateXxx`, `modifyXxx`, `changeXxx`
- 删除：`deleteXxx`, `removeXxx`

## 示例代码

### 标准 Controller 方法示例
```java
@PostMapping("/register")
@Operation(summary = "用户注册", description = "新用户注册账号")
public Result<UserRegisterVO> register(@RequestBody UserRegisterDTO userRegisterDTO) {
    log.info("用户注册：{}", userRegisterDTO.getUsername());
    UserRegisterVO userRegisterVO = userService.userRegister(userRegisterDTO);
    return Result.success(userRegisterVO);
}

@GetMapping("/{userId}")
@Operation(summary = "获取用户信息", description = "根据用户ID获取用户详细信息")
public Result<User> getUserInfo(@Parameter(description = "用户ID") @PathVariable Long userId) {
    log.info("根据id获取用户信息：{}", userId);
    User user = userService.getById(userId);
    user.setPassword(null);
    return Result.success(user);
}

@PutMapping
@Operation(summary = "更新用户信息", description = "更新用户的基本信息")
public Result<String> updateUserInfo(@RequestBody UserUpdateDTO userUpdateDTO) {
    userService.updateUser(userUpdateDTO);
    return Result.success("用户信息更新成功");
}
```

## 注意事项
1. 所有 Controller 方法都应该有 `@Operation` 注解用于 Swagger 文档
2. 关键操作必须记录日志
3. 敏感信息（如密码）不能返回给前端
4. 统一异常处理，避免直接抛出异常
5. DTO 和 VO 分离，不要混用
6. 方法命名要清晰表达业务含义