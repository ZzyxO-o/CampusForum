package cn.zuo.mapper;

import cn.zuo.entity.Notification;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通知数据访问层
 */
@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {
}