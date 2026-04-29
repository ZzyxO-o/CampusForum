# CampusForum 项目代码规范与风格指南

## 重要说明
- **所有对话必须使用中文格式**

## 代码规范

### 1. 导入顺序
- 优先导入项目内部包（cn.zuo.*）
- 然后导入第三方库（io.swagger, jakarta, lombok, org.springframework等）

### 2. 注解使用
- **日志**：使用 `@Slf4j` 注解进行日志记录
- **控制器**：使用 `@RestController` 和 `@RequestMapping` 标记控制器
- **API文档**：使用 Swagger 注解（`@Tag`, `@Operation`, `@Parameter`）进行API文档说明

### 3. 依赖注入
- 使用 `@Resource` 注解而非 `@Autowired`

### 4. 返回值规范
- 控制器方法统一返回 `Result<T>` 类型包装的响应
- 成功：`Result.success(data)`
- 失败：`Result.error(message)`

### 5. 异常处理
- 使用 try-catch 块处理异常
- 使用 `log.error` 记录错误信息

### 6. 日志记录
- 正常操作：`log.info("操作描述：{}", 参数)`
- 错误信息：`log.error("错误描述：{}", e.getMessage())`

### 7. 安全处理
- 不返回敏感字段（如密码），将其设置为 `null`

### 8. RESTful 设计
- 遵循 RESTful 风格，使用合适的 HTTP 方法
- `@GetMapping` - 查询操作
- `@PostMapping` - 创建操作
- `@PutMapping` - 更新操作
- `@DeleteMapping` - 删除操作

### 9. 命名规范
- 类名：大驼峰（如 UserController）
- 方法名：小驼峰（如 register, login, getUserInfo）
- 变量名：小驼峰

### 10. 参数注解
- 路径参数：`@PathVariable`
- 请求体：`@RequestBody`
- 请求参数：`@RequestParam`

### 示例代码结构
```java
@Slf4j
@RestController
@RequestMapping("/users")
@Tag(name = "用户管理", description = "用户信息管理相关接口")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "新用户注册账号")
    public Result<UserRegisterVO> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("用户注册：{}", userRegisterDTO.getUsername());
        try {
            UserRegisterVO userRegisterVO = userService.userRegister(userRegisterDTO);
            return Result.success(userRegisterVO);
        } catch (Exception e) {
            log.error("用户注册失败：{}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }
}
```