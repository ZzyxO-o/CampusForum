package cn.zuo.service.impl;

import cn.zuo.dto.replydto.ReplyDto;
import cn.zuo.dto.replydto.ReplyPageQueryByDiscussionDto;
import cn.zuo.dto.replydto.ReplyUpdateDto;
import cn.zuo.entity.Discussion;
import cn.zuo.entity.Notification;
import cn.zuo.entity.Reply;
import cn.zuo.entity.User;
import cn.zuo.exception.replyException.ReplyQueryException;
import cn.zuo.mapper.DiscussionMapper;
import cn.zuo.mapper.NotificationMapper;
import cn.zuo.mapper.ReplyMapper;
import cn.zuo.mapper.UserMapper;
import cn.zuo.result.PageResult;
import cn.zuo.service.ReplyService;
import cn.zuo.utils.ThreadLocalUtil;
import cn.zuo.vo.replyvo.ReplyMessageVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ReplyServiceImpl extends ServiceImpl<ReplyMapper, Reply> implements ReplyService {

    @Resource
    private ReplyMapper replyMapper;

    @Resource
    private DiscussionMapper discussionMapper;

    @Resource
    private NotificationMapper notificationMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public List<Reply> getRepliesByDiscussionId(Long discussionId) {
        QueryWrapper<Reply> wrapper = new QueryWrapper<>();
        wrapper.eq("discussion_id", discussionId)
                .eq("status", "active")
                .orderByAsc("created_time");
        return replyMapper.selectList(wrapper);
    }

    @Override
    public List<Reply> getRepliesByUserId(Long userId) {
        QueryWrapper<Reply> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .ne("status", "deleted")
                .orderByDesc("created_time");
        return replyMapper.selectList(wrapper);
    }

    @Override
    @Transactional
    public ReplyMessageVo createReply(ReplyDto replyDto) {
        // 验证讨论是否存在且可回复
        Discussion discussion = discussionMapper.selectById(replyDto.getDiscussionId());
        if (discussion == null) {
            throw new ReplyQueryException("讨论不存在");
        }
        if ("closed".equals(discussion.getStatus())) {
            throw new ReplyQueryException("讨论已关闭，无法回复");
        }
        // 创建回复对象
        Reply reply = new Reply();
        BeanUtils.copyProperties(replyDto, reply);
        // 设置回复层级
        if (replyDto.getParentReplyId() != null && replyDto.getParentReplyId() > 0) {
            // 如果是子回复，验证父回复是否存在
            Reply parentReply = replyMapper.selectById(replyDto.getParentReplyId());
            if (parentReply == null) {
                throw new ReplyQueryException("引用的回复不存在");
            }
            // 子回复的层级 = 父回复的层级 + 1
            reply.setReplyLayer(parentReply.getReplyLayer() + 1);
        } else {
            // 顶级回复，层级为0
            reply.setReplyLayer(0);
        }
        // 设置默认值
        Long userId = ThreadLocalUtil.getCurrentId();
        log.info("当前用户ID: {}", userId);
        reply.setUserId(userId);
        reply.setStatus("active");
        reply.setLikeCount(0);
        reply.setCreatedTime(LocalDateTime.now());
        reply.setUpdatedTime(LocalDateTime.now());
        // 插入数据库
        replyMapper.insert(reply);
        User user = userMapper.selectById(userId);
        // 创建通知
        Notification notification = new Notification();
        notification.setUserId(discussion.getUserId());
        notification.setType("reply");
        notification.setRelatedId(reply.getId());
        notification.setSenderId(userId);
        notification.setTitle("回复通知");
        notification.setContent(user.getUsername() + "回复了你的帖子" + discussion.getTitle());
        notification.setIsRead(Boolean.FALSE);
        notification.setCreatedTime(LocalDateTime.now());
        notificationMapper.insert(notification);
        // 更新讨论的回复数
        discussion.setReplyCount(discussion.getReplyCount() + 1);
        discussion.setUpdatedTime(LocalDateTime.now());
        discussionMapper.updateById(discussion);
        // 返回结果
        ReplyMessageVo replyMessageVo = new ReplyMessageVo();
        replyMessageVo.setReplyId(reply.getId());
        replyMessageVo.setMessage("回复创建成功");
        return replyMessageVo;
    }

    @Override
    public void deleteReply(Long replyId) {
        Reply reply = replyMapper.selectById(replyId);
        if (reply == null) {
            throw new ReplyQueryException("回复不存在");
        }

        // 软删除：修改状态为deleted
        reply.setStatus("deleted");
        reply.setUpdatedTime(LocalDateTime.now());
        replyMapper.updateById(reply);

        // 更新讨论的回复数
        Discussion discussion = discussionMapper.selectById(reply.getDiscussionId());
        if (discussion != null && discussion.getReplyCount() > 0) {
            discussion.setReplyCount(discussion.getReplyCount() - 1);
            discussion.setUpdatedTime(LocalDateTime.now());
            discussionMapper.updateById(discussion);
        }
    }

    @Override
    public List<Reply> getChildReplies(Long parentReplyId) {
        QueryWrapper<Reply> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_reply_id", parentReplyId)
                .eq("status", "active")
                .orderByAsc("created_time");
        return replyMapper.selectList(wrapper);
    }

    @Override
    public PageResult getDiscussionReplies(ReplyPageQueryByDiscussionDto replyPageQueryByDiscussionDto) {
        Page<Reply> pageInfo = new Page<>(replyPageQueryByDiscussionDto.getPage(), replyPageQueryByDiscussionDto.getSize());
        QueryWrapper<Reply> wrapper = new QueryWrapper<>();
        wrapper.eq("discussion_id", replyPageQueryByDiscussionDto.getDiscussionId())
                .eq("status", "active");
        
        // 如果指定了回复层级，则按层级筛选
        if (replyPageQueryByDiscussionDto.getReplyLayer() != null) {
            wrapper.eq("reply_layer", replyPageQueryByDiscussionDto.getReplyLayer());
        }
        
        wrapper.orderByAsc("created_time");

        Page<Reply> replyPage = replyMapper.selectPage(pageInfo, wrapper);
        return new PageResult(replyPage.getTotal(), replyPage.getRecords());
    }

    @Override
    public void updateReply(ReplyUpdateDto replyUpdateDto) {
        Reply reply = replyMapper.selectById(replyUpdateDto.getId());
        if (reply == null) {
            throw new ReplyQueryException("回复不存在");
        }

        // 只允许更新内容
        reply.setContent(replyUpdateDto.getContent());
        reply.setUpdatedTime(LocalDateTime.now());
        replyMapper.updateById(reply);
    }

    @Override
    public void hideReply(Long replyId) {
        Reply reply = replyMapper.selectById(replyId);
        if (reply == null) {
            throw new ReplyQueryException("回复不存在");
        }

        reply.setStatus("hidden");
        reply.setUpdatedTime(LocalDateTime.now());
        replyMapper.updateById(reply);
    }

    @Override
    public void showReply(Long replyId) {
        Reply reply = replyMapper.selectById(replyId);
        if (reply == null) {
            throw new ReplyQueryException("回复不存在");
        }

        reply.setStatus("active");
        reply.setUpdatedTime(LocalDateTime.now());
        replyMapper.updateById(reply);
    }

    @Override
    public PageResult getUserReplies(ReplyPageQueryByDiscussionDto replyPageQueryByDiscussionDto) {
        Page<Reply> pageInfo = new Page<>(replyPageQueryByDiscussionDto.getPage(), replyPageQueryByDiscussionDto.getSize());
        QueryWrapper<Reply> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", replyPageQueryByDiscussionDto.getUserId())
                .ne("status", "deleted")
                .orderByDesc("created_time");

        Page<Reply> replyPage = replyMapper.selectPage(pageInfo, wrapper);
        return new PageResult(replyPage.getTotal(), replyPage.getRecords());
    }


}