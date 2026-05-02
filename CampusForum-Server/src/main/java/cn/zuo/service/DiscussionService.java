package cn.zuo.service;

import cn.zuo.dto.discussionsdto.DiscussionDto;
import cn.zuo.dto.discussionsdto.DiscussionPageQueryByUserDto;
import cn.zuo.dto.discussionsdto.DiscussionPageQueryDto;
import cn.zuo.dto.discussionsdto.DiscussionUpdateDto;
import cn.zuo.entity.Discussion;
import cn.zuo.result.PageResult;
import cn.zuo.vo.discussionvo.DiscussionDetailVo;
import cn.zuo.vo.discussionvo.DiscussionMessageVo;
import cn.zuo.vo.discussionvo.HotTitleVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface DiscussionService extends IService<Discussion> {

    /**
     * 分页查询讨论列表
     * @param queryDto
     * @return
     */
    PageResult pageQueryDiscussions(DiscussionPageQueryDto queryDto);

    /**
     * 根据id获取讨论详情
     * @param discussionId
     * @return
     */
    DiscussionDetailVo getDiscussionDetailById(Long discussionId);
    /**
     * 获取所有讨论
     */
    List<Discussion> getAllDiscussions();

    /**
     * 根据ID获取讨论
     */
    Discussion getDiscussionById(Long discussionId);

    /**
     * 根据课程ID获取讨论
     */
    List<Discussion> getDiscussionsByCourseId(Long courseId);

    /**
     * 根据用户ID获取讨论
     */
    List<Discussion> getDiscussionsByUserId(Long userId);

    /**
     * 创建新讨论
     */
    DiscussionMessageVo createDiscussion(DiscussionDto discussionDto);

    /**
     * 更新讨论
     */
    void updateDiscussion(DiscussionUpdateDto discussionUpdateDto);

    /**
     * 删除讨论
     */
    void userDeleteDiscussion(Long discussionId);

    /**
     * 增加浏览次数
     */
    boolean incrementViewCount(Long discussionId);


    /**
     * 根据用户查询讨论
     * @param discussionPageQueryByUserDto
     */
    PageResult getUserDiscussions(DiscussionPageQueryByUserDto discussionPageQueryByUserDto);

    /**
     *根据讨论ID关闭讨论
     * @param discussionId
     */
    void closeDiscussion(Long discussionId);

    /**
     * 获取热门讨论帖
     * @param discussionPageQueryDto
     * @return
     */
    PageResult getHotDiscussions(DiscussionPageQueryDto discussionPageQueryDto);

    /**
     * 热门标题
     * @return
     */
    PageResult<HotTitleVo> getHotTitles();

    /**
     * 管理员删除讨论
     * @param discussionId
     */
    void adminDeleteDiscussion(Long discussionId);

    /**
     * 获取总帖子数
     * @return
     */
    Long getTotalDiscussions();
}