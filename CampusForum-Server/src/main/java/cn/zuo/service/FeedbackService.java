package cn.zuo.service;

import cn.zuo.dto.feedbackdto.FeedbackDto;
import cn.zuo.dto.feedbackdto.FeedbackPageQueryDto;
import cn.zuo.dto.feedbackdto.FeedbackUpdateDto;
import cn.zuo.entity.Feedback;
import cn.zuo.result.PageResult;
import cn.zuo.vo.feedbackvo.FeedbackMessageVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface FeedbackService extends IService<Feedback> {

    /**
     * 用户提交反馈
     */
    FeedbackMessageVo submitFeedback(FeedbackDto feedbackDto);

    /**
     * 用户查看自己的反馈列表（分页）
     */
    PageResult<Feedback> getUserFeedbacks(FeedbackPageQueryDto queryDto);

    /**
     * 根据ID获取反馈详情
     */
    Feedback getFeedbackById(Long id);

    /**
     * 用户删除自己的反馈
     */
    void deleteFeedback(Long id);

    /**
     * 管理员查看所有反馈列表（分页，可按状态筛选）
     */
    PageResult<Feedback> adminGetFeedbacks(FeedbackPageQueryDto queryDto);

    /**
     * 管理员更新反馈状态
     */
    void updateFeedbackStatus(FeedbackUpdateDto updateDto);
}
