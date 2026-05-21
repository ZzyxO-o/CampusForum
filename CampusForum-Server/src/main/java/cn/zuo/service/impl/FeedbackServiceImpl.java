package cn.zuo.service.impl;

import cn.zuo.constant.FeedbackConstants;
import cn.zuo.dto.feedbackdto.FeedbackDto;
import cn.zuo.dto.feedbackdto.FeedbackPageQueryDto;
import cn.zuo.dto.feedbackdto.FeedbackUpdateDto;
import cn.zuo.entity.Feedback;
import cn.zuo.exception.BusinessException;
import cn.zuo.mapper.FeedbackMapper;
import cn.zuo.result.PageResult;
import cn.zuo.service.FeedbackService;
import cn.zuo.utils.ThreadLocalUtil;
import cn.zuo.vo.feedbackvo.FeedbackMessageVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {

    @Resource
    private FeedbackMapper feedbackMapper;

    @Override
    @Transactional
    public FeedbackMessageVo submitFeedback(FeedbackDto feedbackDto) {
        Feedback feedback = new Feedback();
        BeanUtils.copyProperties(feedbackDto, feedback);
        feedback.setUserId(ThreadLocalUtil.getCurrentId());
        feedback.setStatus(FeedbackConstants.FEEDBACK_STATUS_PENDING);
        feedback.setCreatedTime(LocalDateTime.now());
        feedback.setUpdatedTime(LocalDateTime.now());
        feedbackMapper.insert(feedback);

        FeedbackMessageVo vo = new FeedbackMessageVo();
        vo.setFeedbackId(feedback.getId());
        vo.setMessage(FeedbackConstants.FEEDBACK_SUBMIT_SUCCESS);
        return vo;
    }

    @Override
    public PageResult<Feedback> getUserFeedbacks(FeedbackPageQueryDto queryDto) {
        Long userId = ThreadLocalUtil.getCurrentId();
        Page<Feedback> pageInfo = new Page<>(queryDto.getPage(), queryDto.getSize());
        QueryWrapper<Feedback> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .orderByDesc("created_time");
        if (queryDto.getStatus() != null && !queryDto.getStatus().isEmpty()) {
            wrapper.eq("status", queryDto.getStatus());
        }
        Page<Feedback> feedbackPage = feedbackMapper.selectPage(pageInfo, wrapper);
        return new PageResult<>(feedbackPage.getTotal(), feedbackPage.getRecords());
    }

    @Override
    public Feedback getFeedbackById(Long id) {
        Feedback feedback = feedbackMapper.selectById(id);
        if (feedback == null) {
            throw new BusinessException(FeedbackConstants.FEEDBACK_NOT_FOUND);
        }
        // 用户只能查看自己的反馈
        if (!feedback.getUserId().equals(ThreadLocalUtil.getCurrentId())) {
            throw new BusinessException(FeedbackConstants.FEEDBACK_PERMISSION_DENIED);
        }
        return feedback;
    }

    @Override
    public void deleteFeedback(Long id) {
        Feedback feedback = feedbackMapper.selectById(id);
        if (feedback == null) {
            throw new BusinessException(FeedbackConstants.FEEDBACK_NOT_FOUND);
        }
        if (!feedback.getUserId().equals(ThreadLocalUtil.getCurrentId())) {
            throw new BusinessException(FeedbackConstants.FEEDBACK_PERMISSION_DENIED);
        }
        feedbackMapper.deleteById(id);
    }

    @Override
    public PageResult<Feedback> adminGetFeedbacks(FeedbackPageQueryDto queryDto) {
        Page<Feedback> pageInfo = new Page<>(queryDto.getPage(), queryDto.getSize());
        QueryWrapper<Feedback> wrapper = new QueryWrapper<>();
        if (queryDto.getStatus() != null && !queryDto.getStatus().isEmpty()) {
            wrapper.eq("status", queryDto.getStatus());
        }
        wrapper.orderByDesc("created_time");
        Page<Feedback> feedbackPage = feedbackMapper.selectPage(pageInfo, wrapper);
        return new PageResult<>(feedbackPage.getTotal(), feedbackPage.getRecords());
    }

    @Override
    public void updateFeedbackStatus(FeedbackUpdateDto updateDto) {
        Feedback feedback = feedbackMapper.selectById(updateDto.getId());
        if (feedback == null) {
            throw new BusinessException(FeedbackConstants.FEEDBACK_NOT_FOUND);
        }
        feedback.setStatus(updateDto.getStatus());
        feedback.setUpdatedTime(LocalDateTime.now());
        feedbackMapper.updateById(feedback);
    }
}
