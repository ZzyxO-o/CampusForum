package cn.zuo.service.impl;

import cn.zuo.constant.JwtClaimsConstant;
import cn.zuo.constant.RedisConstants;
import cn.zuo.constant.businessConstant.UserConstants;
import cn.zuo.dto.userdto.*;
import cn.zuo.entity.*;
import cn.zuo.exception.BusinessException;
import cn.zuo.exception.userException.*;
import cn.zuo.mapper.*;
import cn.zuo.properties.JwtProperties;
import cn.zuo.result.PageResult;
import cn.zuo.service.UserService;
import cn.zuo.utils.JwtUtil;
import cn.zuo.utils.ThreadLocalUtil;
import cn.zuo.vo.admin.AdminUserStatsVo;
import cn.zuo.vo.admin.DailyStatVo;
import cn.zuo.vo.admin.NewStatsVo;
import cn.zuo.vo.uservo.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private DiscussionMapper discussionMapper;

    @Resource
    private ReplyMapper replyMapper;

    @Resource
    private LikeMapper likeMapper;

    @Resource
    private FavoriteMapper favoriteMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private JwtProperties jwtProperties;

    @Override
    public UserRegisterVO userRegister(UserRegisterDTO userRegisterDTO) {
        // 1. 检查用户名是否已存在
        User existUser = getUserByUsername(userRegisterDTO.getUsername());
        if (existUser != null) {
            throw new UserRegisterException("用户名已存在");
        }
        // 2. 创建用户对象
        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        user.setPassword(userRegisterDTO.getPassword());
        user.setNickname(userRegisterDTO.getNickname());
        user.setCollege(userRegisterDTO.getCollege());
        user.setEmail(userRegisterDTO.getEmail());
        if (userRegisterDTO.getBio() == null || userRegisterDTO.getBio().isEmpty()) {
            user.setBio(UserConstants.USER_DEFAULT_BIO);
        } else {
            user.setBio(userRegisterDTO.getBio());
        }
        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedTime(LocalDateTime.now());
        // 3. 设置默认值
        user.setRole(UserConstants.USER_DEFAULT_ROLE_USER);
        user.setStatus(UserConstants.USER_DEFAULT_STATUS_ACTIVE);
        // 4. 密码md5进行加密
        if (user.getPassword() != null) {
            String hashedPassword = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
            user.setPassword(hashedPassword);
        }
        // 5. 设置默认头像（如果没有提供）
        if (user.getAvatarUrl() == null || user.getAvatarUrl().isEmpty()) {
            user.setAvatarUrl(UserConstants.USER_DEFAULT_AVATAR_URL);
        }
        // 6. 保存用户
        int result = userMapper.insert(user);
        if (result <= 0) {
            throw new UserRegisterException("注册失败，请稍后重试");
        }
        // 7. 构建返回对象
        UserRegisterVO userRegisterVO = new UserRegisterVO();
        userRegisterVO.setId(user.getId());
        userRegisterVO.setUsername(user.getUsername());
        userRegisterVO.setNickname(user.getNickname());
        userRegisterVO.setCollege(user.getCollege());
        userRegisterVO.setAvatarUrl(user.getAvatarUrl());
        userRegisterVO.setRole(user.getRole());
        userRegisterVO.setStatus(user.getStatus());
        userRegisterVO.setCreatedTime(user.getCreatedTime());
        log.info("用户注册成功，用户ID：{}，用户名：{}", user.getId(), user.getUsername());
        return userRegisterVO;
    }

    @Override
    public UserLoginVO userLogin(@NotNull UserLoginDTO userLoginDTO) {
        // 1. 查找用户
        User user = getUserByUsername(userLoginDTO.getUsername());
        if (user == null) {
            throw new UserLoginException("用户名或密码错误");
        }
        // 2. 检查用户状态
        if (!"ACTIVE".equals(user.getStatus())) {
            throw new UserLoginException("账号已被禁用");
        }
        // 3. 验证密码
        String hashedPassword = DigestUtils.md5DigestAsHex(userLoginDTO.getPassword().getBytes());
        if (!user.getPassword().equals(hashedPassword)) {
            throw new UserLoginException("用户名或密码错误");
        }
        // 4. 生成JWT token 携带用户ID
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getSecretKey(), jwtProperties.getExpirationTime(), claims);
        // 5. 将token存储到Redis中，用于后续的登出管理
        String redisKey = RedisConstants.TOKEN_PREFIX + user.getUsername();
        redisTemplate.opsForValue().set(redisKey, token);
        // 设置与JWT相同的过期时间（毫秒转秒）
        redisTemplate.expire(redisKey, jwtProperties.getExpirationTime() / 1000, TimeUnit.SECONDS);
        // 6. 添加用户到在线用户集合
        redisTemplate.opsForSet().add(RedisConstants.ONLINE_USERS_KEY, user.getId().toString());
        // 7. 今日登录次数+1
        redisTemplate.opsForValue().increment(RedisConstants.DAILY_LOGIN_COUNT_KEY);
        // 设置过期时间为24小时，每天自动重置
        redisTemplate.expire(RedisConstants.DAILY_LOGIN_COUNT_KEY, RedisConstants.DAILY_EXPIRE, TimeUnit.SECONDS);
        // 8. 构建返回对象
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setId(user.getId());
        userLoginVO.setAuthorization(token);
        log.info("用户登录成功，用户ID：{}，用户名：{}", user.getId(), user.getUsername());
        return userLoginVO;
    }

    @Override
    public User getUserByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        return userMapper.selectOne(wrapper);
    }

    /**
     * 获取当前用户统计信息
     * @return
     */
    @Override
    public UserStatsVo getUserStatsByUserId(Long userId) {
        UserStatsVo userStatsVo = new UserStatsVo();
        QueryWrapper<Discussion> discussionQueryWrapper = new QueryWrapper<>();
        discussionQueryWrapper.eq("user_id", userId);
        userStatsVo.setPostCount(discussionMapper.selectCount(discussionQueryWrapper));
        QueryWrapper<Reply> replyWrapper = new QueryWrapper<>();
        replyWrapper.eq("user_id", userId);
        userStatsVo.setReplyCount(replyMapper.selectCount(replyWrapper));
        QueryWrapper<Like> likeWrapper = new QueryWrapper<>();
        likeWrapper.eq("user_id", userId);
        userStatsVo.setLikeCount(likeMapper.selectCount(likeWrapper));
        QueryWrapper<Favorite> favoriteWrapper = new QueryWrapper<>();
        favoriteWrapper.eq("user_id", userId);
        userStatsVo.setFavoriteCount(favoriteMapper.selectCount(favoriteWrapper));
        return userStatsVo;
    }

    /**
     * 获取系统统计信息
     * @return
     */
    @Override
    public UserOverviewDataVo getSystemOverview() {
        UserOverviewDataVo userOverviewDataVo = new UserOverviewDataVo();
        Long totalUsers = getCachedCount(RedisConstants.TOTAL_USERS_KEY, () -> userMapper.selectCount(new QueryWrapper<User>()));
        Long totalDiscussions = getCachedCount(RedisConstants.TOTAL_DISCUSSIONS_KEY, () -> discussionMapper.selectCount(new QueryWrapper<Discussion>()));
        userOverviewDataVo.setTotalUsers(totalUsers);
        userOverviewDataVo.setTotalDiscussions(totalDiscussions);
        return userOverviewDataVo;
    }

    private Long getCachedCount(String key, java.util.function.Supplier<Long> countSupplier) {
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached instanceof Number) {
            return ((Number) cached).longValue();
        }
        Long count = countSupplier.get();
        redisTemplate.opsForValue().set(key, count.toString(), RedisConstants.CACHE_EXPIRE, TimeUnit.SECONDS);
        return count;
    }

    @Override
    public NewStatsVo getNewStats(Integer days) {
        LocalDateTime startTime = LocalDate.now().minusDays(days - 1).atStartOfDay();
        LocalDateTime endTime = LocalDate.now().atTime(LocalTime.MAX);

        NewStatsVo vo = new NewStatsVo();
        vo.setNewUsers(userMapper.selectCount(new LambdaQueryWrapper<User>()
                .ge(User::getCreatedTime, startTime)
                .le(User::getCreatedTime, endTime)));
        vo.setNewDiscussions(discussionMapper.selectCount(new LambdaQueryWrapper<Discussion>()
                .ge(Discussion::getCreatedTime, startTime)
                .le(Discussion::getCreatedTime, endTime)));
        vo.setNewReplies(replyMapper.selectCount(new LambdaQueryWrapper<Reply>()
                .ge(Reply::getCreatedTime, startTime)
                .le(Reply::getCreatedTime, endTime)));
        return vo;
    }

    @Override
    public List<DailyStatVo> getTrend(Integer days) {
        List<DailyStatVo> stats = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
            Long newUsers = userMapper.selectCount(new LambdaQueryWrapper<User>()
                    .ge(User::getCreatedTime, startOfDay)
                    .le(User::getCreatedTime, endOfDay));
            Long newDiscussions = discussionMapper.selectCount(new LambdaQueryWrapper<Discussion>()
                    .ge(Discussion::getCreatedTime, startOfDay)
                    .le(Discussion::getCreatedTime, endOfDay));
            Long newReplies = replyMapper.selectCount(new LambdaQueryWrapper<Reply>()
                    .ge(Reply::getCreatedTime, startOfDay)
                    .le(Reply::getCreatedTime, endOfDay));
            stats.add(new DailyStatVo(date, newUsers, newDiscussions, newReplies));
        }
        return stats;
    }

    @Override
    public User findByUsername(String username) {
        User userByUsername = getUserByUsername(username);
        if (userByUsername == null) {
            throw new BusinessException("用户不存在");
        }
        // 不返回密码字段
        userByUsername.setPassword(null);
        return userByUsername;
    }

    @Override
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public void updateUserById(UserUpdateDTO userUpdateDTO) {
        //要修改的用户id
        Long userId = userUpdateDTO.getUserId();
        //当前用户信息
        User user = userMapper.selectById(ThreadLocalUtil.getCurrentId());
        //判断要更新的用户是否是当前用户
        if (!userId.equals(ThreadLocalUtil.getCurrentId())) {
            //如果要更新其他用户的个人信息，判断是否是管理员工
            if (!"admin".equals(user.getRole())) {
                throw new UserUpdateException("您没有权限更新其他用户的个人信息");
            }
        }
        // 只更新非敏感字段
        User updateUser = userMapper.selectById(userId);
        updateUser.setNickname(userUpdateDTO.getNickname());
        updateUser.setCollege(userUpdateDTO.getCollege());
        updateUser.setAvatarUrl(userUpdateDTO.getAvatarUrl());
        updateUser.setBio(userUpdateDTO.getBio());
        updateUser.setPhone(userUpdateDTO.getPhone());
        updateUser.setUpdatedTime(LocalDateTime.now());
        userMapper.updateById(updateUser);
    }

    @Override
    public void changePassword(UserChangePasswordDTO userChangePasswordDTO) {
        //要修改密码的用户id
        Long userId = userChangePasswordDTO.getUserId();

        //判断要修改密码的用户是否是当前用户
        if (!userId.equals(ThreadLocalUtil.getCurrentId())) {
            throw new UserChangePasswordException("您没有权限修改其他用户的密码");
        }
        // 获取用户当前信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new UserChangePasswordException("用户不存在");
        }
        // 验证旧密码
        String hashedOldPassword = DigestUtils.md5DigestAsHex(userChangePasswordDTO.getOldPassword().getBytes());
        if (!user.getPassword().equals(hashedOldPassword)) {
            throw new UserChangePasswordException("旧密码错误");
        }
        // 更新新密码
        User updateUser = new User();
        updateUser.setId(userId);
        String hashedNewPassword = DigestUtils.md5DigestAsHex(userChangePasswordDTO.getNewPassword().getBytes());
        updateUser.setPassword(hashedNewPassword);
        updateUser.setUpdatedTime(LocalDateTime.now());
        userMapper.updateById(updateUser);
    }

    @Override
    public void deleteUserById(Long userId) {
        //判断要删除的用户是否是当前用户
        if (!userId.equals(ThreadLocalUtil.getCurrentId())) {
            //判断当前用户是不是管理员工
            User user = userMapper.selectById(ThreadLocalUtil.getCurrentId());
            if (!"admin".equals(user.getRole())) {
                throw new UserDeleteException("您没有权限删除其他用户的账号");
            }
        }

        // 软删除：将用户状态改为inactive
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new UserDeleteException("用户不存在");
        }
        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setStatus("INACTIVE"); // 软删除，标记为不活跃
        updateUser.setUpdatedTime(LocalDateTime.now());
        userMapper.updateById(updateUser);
    }

    @Override
    public void updateUserStatus(Long userId, String status) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new UserUpdateException("用户不存在");
        }
        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setStatus(status);
        updateUser.setUpdatedTime(LocalDateTime.now());
        userMapper.updateById(updateUser);
    }

    @Override
    public void updateUserRole(Long userId, String role) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new UserUpdateException("用户不存在");
        }
        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setRole(role);
        updateUser.setUpdatedTime(LocalDateTime.now());
        userMapper.updateById(updateUser);
    }

    /**
     * 用户登出
     * @param token 用户token
     * @return
     */
    @Override
    public void logout(String token) {
        // 1. 从token中提取用户名
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
        Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
        User user = userMapper.selectById(userId);
        if (user == null) {
            log.error("登出时用户不存在，userId：{}", userId);
            throw new UserLogoutException("用户不存在");
        }

        // 2. 从Redis中删除token
        String redisKey = RedisConstants.TOKEN_PREFIX + user.getUsername();
        redisTemplate.delete(redisKey);

        // 3. 将token添加到黑名单（用于立即失效）
        String blacklistKey = RedisConstants.TOKEN_BLACKLIST_PREFIX + token;
        // 设置黑名单token的过期时间为token过期时间，确保它不会永久存在
        long expirationTime = jwtProperties.getExpirationTime() / 1000;
        redisTemplate.opsForValue().set(blacklistKey, "invalid", expirationTime, TimeUnit.SECONDS);

        // 4. 从在线用户集合中移除用户
        redisTemplate.opsForSet().remove(RedisConstants.ONLINE_USERS_KEY, user.getId().toString());

        log.info("用户登出成功，用户名：{}", user.getUsername());
    }

    /**
     * 获取登录统计数据
     * @return 包含在线用户数和今日登录次数的Map
     */
    @Override
    public UserLoginStatsVo getLoginStats() {
        UserLoginStatsVo stats = new UserLoginStatsVo();
        // 获取在线用户数
        Long onlineCount = redisTemplate.opsForSet().size(RedisConstants.ONLINE_USERS_KEY);
        stats.setOnlineUsers(onlineCount != null ? onlineCount : 0);
        // 获取今日登录次数
        Object dailyCountObj = redisTemplate.opsForValue().get(RedisConstants.DAILY_LOGIN_COUNT_KEY);
        Long dailyCount = 0L;
        if (dailyCountObj instanceof Number) {
            dailyCount = ((Number) dailyCountObj).longValue();
        }
        stats.setDailyLoginCount(dailyCount);
        return stats;
    }

    /**
     * 获取用户列表（分页）
     * @param userPageQueryDto
     * @return
     */
    @Override
    public PageResult getUserList(UserPageQueryDto userPageQueryDto) {
        Page<User> pageInfo = new Page<>(userPageQueryDto.getPage(), userPageQueryDto.getSize());
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        // 添加搜索条件
        if (userPageQueryDto.getUsername() != null && !userPageQueryDto.getUsername().trim().isEmpty()) {
            wrapper.like("username", userPageQueryDto.getUsername().trim());
        }
        if (userPageQueryDto.getStatus() != null && !userPageQueryDto.getStatus().trim().isEmpty()) {
            wrapper.eq("status", userPageQueryDto.getStatus().trim());
        }
        // 不返回密码字段
        wrapper.select("id", "username", "nickname","college", "email", "avatar_url", "bio","phone", "role", "status", "created_time", "updated_time");
        Page<User> userPage = userMapper.selectPage(pageInfo, wrapper);
        return new PageResult(userPage.getTotal(), userPage.getRecords());
    }

    /**
     * 获取活跃用户列表（单条SQL聚合，避免N+1查询）
     * 活跃度 = 发帖数×3 + 回帖数×2 + 获得点赞数×1
     */
    @Override
    public List<ActiveUserVo> getActiveUsers(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        return userMapper.selectActiveUsers(limit);
    }

    /**
     * 用户统计
     * @return
     */
    @Override
    public AdminUserStatsVo getUserStats() {
        AdminUserStatsVo userStatsVo = new AdminUserStatsVo();
        // 统计总用户数
        QueryWrapper<User> totalWrapper = new QueryWrapper<>();
        userStatsVo.setTotalUsers(userMapper.selectCount(totalWrapper));
        //统计活跃用户数
        QueryWrapper<User> activeWrapper = new QueryWrapper<>();
        activeWrapper.eq("status", "ACTIVE");
        userStatsVo.setTotalActiveUsers(userMapper.selectCount(activeWrapper));
        //统计不活跃用户数
        QueryWrapper<User> inactiveWrapper = new QueryWrapper<>();
        inactiveWrapper.eq("status", "INACTIVE");
        userStatsVo.setTotalInactiveUsers(userMapper.selectCount(inactiveWrapper));
        //统计被禁用用户数
        QueryWrapper<User> bannedWrapper = new QueryWrapper<>();
        bannedWrapper.eq("status", "BANNED");
        userStatsVo.setTotalBannedUsers(userMapper.selectCount(bannedWrapper));
        return userStatsVo;
    }

}