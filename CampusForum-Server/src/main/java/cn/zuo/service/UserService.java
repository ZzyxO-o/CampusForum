package cn.zuo.service;

import cn.zuo.dto.userdto.*;
import cn.zuo.entity.User;
import cn.zuo.result.PageResult;
import cn.zuo.vo.admin.AdminUserStatsVo;
import cn.zuo.vo.admin.DailyStatVo;
import cn.zuo.vo.uservo.*;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserService extends IService<User> {
    /**
     * 根据用户名查找用户
     */
    User getUserByUsername(String username);

    /**
     * 根据用户名查找用户（用于登录）
     */
    User findByUsername(String username);

    /**
     * 根据用户ID查找用户
     */
    User getUserById(Long id);

    /**
     * 更新用户信息
     */
    void updateUserById(UserUpdateDTO userUpdateDTO);

    /**
     * 修改用户密码
     */
    void changePassword(UserChangePasswordDTO userChangePasswordDTO);

    /**
     * 根据用户ID删除用户（软删除）
     */
    void deleteUserById(Long userId);

    /**
     * 更新用户状态
     */
    void updateUserStatus(Long userId, String status);

    /**
     * 更新用户角色
     */
    void updateUserRole(Long userId, String role);


    /**
     * 用户注册（带参数验证）
     */
    UserRegisterVO userRegister(UserRegisterDTO userRegisterDTO);

    /**
     * 用户登录
     */
    UserLoginVO userLogin(UserLoginDTO userLoginDTO);

    /**
     * 用户登出
     * @param token 用户token
     * @return 登出是否成功
     */
    void logout(String token);

    /**
     * 获取登录统计数据
     * @return 包含在线用户数和今日登录次数的Map
     */
    UserLoginStatsVo getLoginStats();

    /**
     * 获取用户列表（分页）
     * @param queryDto
     * @return
     */
    PageResult getUserList(UserPageQueryDto queryDto);

    /**
     * 用户统计
     * @return
     */
    AdminUserStatsVo getUserStats();

    /**\
     * 获取当前用户统计信息
     * @return
     */
    UserStatsVo getUserStatsByUserId(Long userId);

    /**
     * 获取系统总览数据
     * @return
     */
    UserOverviewDataVo getSystemOverview();

    /**
     * 获取最近N天的新增汇总统计
     * @param days
     * @return
     */
    cn.zuo.vo.admin.NewStatsVo getNewStats(Integer days);

    /**
     * 获取最近N天的每日趋势明细（折线图用）
     * @param days
     * @return
     */
    List<DailyStatVo> getTrend(Integer days);
}