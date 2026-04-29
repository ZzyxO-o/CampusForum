package cn.zuo.service.impl;

import cn.zuo.dto.notificationdto.NotificationPageQueryDto;
import cn.zuo.entity.Notification;
import cn.zuo.exception.BusinessException;
import cn.zuo.mapper.NotificationMapper;
import cn.zuo.result.PageResult;
import cn.zuo.service.NotificationService;
import cn.zuo.utils.ThreadLocalUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 通知服务层实现
 */
@Slf4j
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    @Resource
    private NotificationMapper notificationMapper;

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
}