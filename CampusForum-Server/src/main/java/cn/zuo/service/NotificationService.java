package cn.zuo.service;

import cn.zuo.dto.notificationdto.AdminNotificationDto;
import cn.zuo.dto.notificationdto.NotificationPageQueryDto;
import cn.zuo.entity.Notification;
import cn.zuo.result.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 通知服务层接口
 */
public interface NotificationService extends IService<Notification> {

    /**
     * 获取用户的未读通知数量
     * @return 未读通知数量
     */
    int getUnreadCount();

    /**
     * 获取用户的通知列表
     * @param notificationPageQueryDto 查询条件
     * @return 分页结果
     */
    PageResult<Notification> getUserNotifications(NotificationPageQueryDto notificationPageQueryDto);

    /**
     * 标记通知为已读
     * @param notificationId 通知ID
     */
    void markAsRead(Long notificationId);

    /**
     * 标记所有通知为已读
     */
    void markAllAsRead();

    /**
     * 创建通知
     * @param notification 通知信息
     * @return 是否创建成功
     */
    boolean createNotification(Notification notification);

    /**
     * 删除所有通知
     */
    void deleteAllNotifications(Long userId);

    /**
     * 发送系统通知
     * @param adminNotificationDto
     */
    void sendSystemNotification(AdminNotificationDto adminNotificationDto);

    /**
     * 发送广播通知
     * @param adminNotificationDto
     */
    void sendBroadcastNotification(AdminNotificationDto adminNotificationDto);
}