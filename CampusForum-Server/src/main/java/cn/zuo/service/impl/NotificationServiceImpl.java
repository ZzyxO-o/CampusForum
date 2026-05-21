package cn.zuo.service.impl;

import cn.zuo.dto.notificationdto.AdminNotificationDto;
import cn.zuo.dto.notificationdto.NotificationPageQueryDto;
import cn.zuo.entity.Notification;
import cn.zuo.entity.User;
import cn.zuo.exception.BusinessException;
import cn.zuo.mapper.NotificationMapper;
import cn.zuo.mapper.UserMapper;
import cn.zuo.result.PageResult;
import cn.zuo.service.NotificationService;
import cn.zuo.utils.ThreadLocalUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 通知服务层实现
 */
@Slf4j
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    @Resource
    private NotificationMapper notificationMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public int getUnreadCount() {
        Long userId = ThreadLocalUtil.getCurrentId();
        QueryWrapper<Notification> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .eq("is_read", false);
        return Math.toIntExact(notificationMapper.selectCount(wrapper));
    }

    @Override
    public PageResult<Notification> getUserNotifications(NotificationPageQueryDto notificationPageQueryDto) {
        Long userId = notificationPageQueryDto.getUserId();
        //判断是否是当前用户的通知
        if (!userId.equals(ThreadLocalUtil.getCurrentId())) {
            throw new BusinessException("您没有权限查看其他用户的通知");
        }
        Page<Notification> pageInfo = new Page<>(notificationPageQueryDto.getPage(), notificationPageQueryDto.getSize());
        QueryWrapper<Notification> wrapper = new QueryWrapper<>();
        if (!notificationPageQueryDto.getType().equals("all")) {
            wrapper.eq("type", notificationPageQueryDto.getType());
        }
        wrapper.eq("user_id", userId)
                .orderByDesc("created_time");
        Page<Notification> notificationPage = notificationMapper.selectPage(pageInfo, wrapper);
        return new PageResult<>(notificationPage.getTotal(), notificationPage.getRecords());
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = notificationMapper.selectById(notificationId);
        if (notification == null) {
            throw new RuntimeException("通知不存在");
        }
        notification.setIsRead(true);
        notificationMapper.updateById(notification);
    }

    @Override
    public void markAllAsRead() {
        Long userId = ThreadLocalUtil.getCurrentId();
        QueryWrapper<Notification> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .eq("is_read", false);
        Notification updateNotification = new Notification();
        updateNotification.setIsRead(true);
        notificationMapper.update(updateNotification, wrapper);
    }

    @Override
    public boolean createNotification(Notification notification) {
        notification.setIsRead(false);
        return notificationMapper.insert(notification) > 0;
    }

    @Override
    public void deleteAllNotifications(Long userId) {
        if (!userId.equals(ThreadLocalUtil.getCurrentId())) {
            throw new BusinessException("您没有权限删除其他用户的通知");
        }
        QueryWrapper<Notification> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        notificationMapper.delete(wrapper);
    }

    @Override
    public void sendSystemNotification(AdminNotificationDto adminNotificationDto) {
        Notification notification = new Notification();
        notification.setUserId(adminNotificationDto.getUserId());
        notification.setSenderId(adminNotificationDto.getAdminId());
        notification.setTitle(adminNotificationDto.getTitle());
        notification.setContent(adminNotificationDto.getContent());
        notification.setType("system");
        notification.setIsRead(false);
        notification.setCreatedTime(LocalDateTime.now());
        notificationMapper.insert(notification);
        log.info("系统通知发送成功: targetUserId={}, title={}", adminNotificationDto.getUserId(), adminNotificationDto.getTitle());
    }

    @Override
    @Transactional
    public void sendBroadcastNotification(AdminNotificationDto adminNotificationDto) {
        // 查询所有活跃用户
        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.eq("status", "ACTIVE")
                   .select("id"); // 只查询用户ID，提高性能

        List<User> users = userMapper.selectList(userWrapper);

        if (users.isEmpty()) {
            log.info("没有活跃用户，跳过广播通知发送");
            return;
        }
        // 批量创建通知
        List<Notification> notifications = new ArrayList<>();
        for (User user : users) {
            Notification notification = new Notification();
            notification.setUserId(user.getId());
            notification.setSenderId(adminNotificationDto.getAdminId());
            notification.setTitle(adminNotificationDto.getTitle());
            notification.setContent(adminNotificationDto.getContent());
            notification.setType("broadcast"); // 广播通知类型
            notification.setIsRead(false);
            notification.setCreatedTime(LocalDateTime.now());
            notifications.add(notification);
        }
        //批量插入通知
        this.saveBatch(notifications);
        log.info("广播通知发送成功，共发送给 {} 个用户", notifications.size());
    }
}