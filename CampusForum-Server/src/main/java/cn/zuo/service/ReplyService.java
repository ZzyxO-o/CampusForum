package cn.zuo.service;

import cn.zuo.dto.replydto.ReplyDto;
import cn.zuo.dto.replydto.ReplyPageQueryByDiscussionDto;
import cn.zuo.dto.replydto.ReplyUpdateDto;
import cn.zuo.entity.Reply;
import cn.zuo.result.PageResult;
import cn.zuo.vo.replyvo.ReplyMessageVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ReplyService extends IService<Reply> {

    /**
     * 根据讨论ID获取回复
     * @param discussionId 讨论ID
     * @return 回复列表
     */
    List<Reply> getRepliesByDiscussionId(Long discussionId);

    /**
     * 根据用户ID获取回复
     * @param userId 用户ID
     * @return 回复列表
     */
    List<Reply> getRepliesByUserId(Long userId);

    /**
     * 创建回复
     * @param replyDto 回复信息
     * @return 创建结果（包含回复ID和消息）
     */
    ReplyMessageVo createReply(ReplyDto replyDto);

    /**
     * 删除回复
     * @param replyId 回复ID
     */
    void deleteReply(Long replyId);

    /**
     * 获取子回复（针对某条回复的回复）
     * @param parentReplyId 父回复ID
     * @return 子回复列表
     */
    List<Reply> getChildReplies(Long parentReplyId);

    /**
     * 根据讨论id获取所有回复
     * @param replyPageQueryByDiscussionDto 查询条件
     * @return 分页结果
     */
    PageResult getDiscussionReplies(ReplyPageQueryByDiscussionDto replyPageQueryByDiscussionDto);

    /**
     * 更新回复
     * @param replyUpdateDto 更新信息
     */
    void updateReply(ReplyUpdateDto replyUpdateDto);

    /**
     * 隐藏回复
     * @param replyId 回复ID
     */
    void hideReply(Long replyId);

    /**
     * 显示回复
     * @param replyId 回复ID
     */
    void showReply(Long replyId);

    /**
     * 获取用户回复
     * @param replyPageQueryByDiscussionDto 查询条件
     * @return 分页结果
     */
    PageResult getUserReplies(ReplyPageQueryByDiscussionDto replyPageQueryByDiscussionDto);


}