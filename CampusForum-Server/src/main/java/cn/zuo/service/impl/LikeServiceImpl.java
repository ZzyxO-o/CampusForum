package cn.zuo.service.impl;

import cn.zuo.dto.likedto.LikeDto;
import cn.zuo.dto.likedto.LikePageQueryDto;
import cn.zuo.entity.*;
import cn.zuo.mapper.*;
import cn.zuo.result.PageResult;
import cn.zuo.service.LikeService;
import cn.zuo.vo.likevo.LikeMessageVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> implements LikeService {

    @Resource
    private LikeMapper likeMapper;

    @Resource
    private NotificationMapper notificationMapper;

    @Resource
    private DiscussionMapper discussionMapper;

    @Resource
    private ReplyMapper replyMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    @Transactional
    public LikeMessageVo toggleLike(LikeDto likeDto) {
        Long userId = likeDto.getUserId();
        String targetType = likeDto.getTargetType();
        Long targetId = likeDto.getTargetId();

        // 查询是否已存在点赞记录
        QueryWrapper<Like> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .eq("target_type", targetType)
                .eq("target_id", targetId);

        Like existingLike = this.getOne(wrapper);

        String tableName = "discussion".equals(targetType) ? "discussions" : "replies";
        boolean isLiked;
        String message;

        if (existingLike != null) {
            // 已存在记录，切换状态
            if (existingLike.getStatus() == 1) {
                // 当前是点赞状态，取消点赞
                existingLike.setStatus(0);
                this.updateById(existingLike);
                likeMapper.decrementLikeCount(tableName, targetId);
                isLiked = false;
                message = "取消点赞";
            } else {
                // 当前是取消状态，恢复点赞
                existingLike.setStatus(1);
                this.updateById(existingLike);
                likeMapper.incrementLikeCount(tableName, targetId);
                isLiked = true;
                message = "点赞成功";
            }
        } else {
            // 不存在记录，创建新点赞
            Like newLike = new Like();
            newLike.setUserId(userId);
            newLike.setTargetType(targetType);
            newLike.setTargetId(targetId);
            newLike.setStatus(1);
            newLike.setCreatedTime(LocalDateTime.now());
            this.save(newLike);
            likeMapper.incrementLikeCount(tableName, targetId);
            // 创建点赞通知
            Notification notification = new Notification();
            //根据targetType判断是帖子还是回复
            Long targetUserId;
            if (targetType.equals("discussion")) {
                Discussion discussion = discussionMapper.selectById(targetId);
                targetUserId = discussion.getUserId();
            } else {
                Reply reply = replyMapper.selectById(targetId);
                targetUserId = reply.getUserId();
            }
            User user = userMapper.selectById(userId);
            notification.setUserId(targetUserId);
            notification.setType("like");
            notification.setRelatedId(targetId);
            notification.setSenderId(userId);
            notification.setTitle("点赞通知");
            notification.setContent(user.getNickname() + "点赞了你的帖子");
            notification.setIsRead(false);
            notification.setCreatedTime(LocalDateTime.now());
            notificationMapper.insert(notification);
            isLiked = true;
            message = "点赞成功";
        }

        // 构建返回结果
        LikeMessageVo likeVo = new LikeMessageVo();
        likeVo.setLiked(isLiked);
        likeVo.setLikeCount(getLikeCount(targetType, targetId));
        likeVo.setMessage(message);
        return likeVo;
    }

    @Override
    public boolean isLiked(LikeDto likeDto) {
        Long userId = likeDto.getUserId();
        QueryWrapper<Like> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .eq("target_type", likeDto.getTargetType())
                .eq("target_id", likeDto.getTargetId())
                .eq("status", 1);
        return this.count(wrapper) > 0;
    }

    @Override
    public int getLikeCount(String targetType, Long targetId) {
        QueryWrapper<Like> wrapper = new QueryWrapper<>();
        wrapper.eq("target_type", targetType)
                .eq("target_id", targetId)
                .eq("status", 1);
        return Math.toIntExact(this.count(wrapper));
    }

    @Override
    public PageResult<Like> getUserLikes(LikePageQueryDto queryDto) {
        Page<Like> page = new Page<>(queryDto.getPage(), queryDto.getSize());
        QueryWrapper<Like> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", queryDto.getUserId())
                .eq("target_type", queryDto.getTargetType())
                .eq("status", 1);
        Page<Like> likePage = likeMapper.selectPage(page, wrapper);
        return new PageResult<>(likePage.getTotal(), likePage.getRecords());
    }
}