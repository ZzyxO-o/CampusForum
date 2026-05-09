package cn.zuo.service.impl;

import cn.zuo.dto.discussionsdto.DiscussionDto;
import cn.zuo.dto.discussionsdto.DiscussionPageQueryByUserDto;
import cn.zuo.dto.discussionsdto.DiscussionPageQueryDto;
import cn.zuo.dto.discussionsdto.DiscussionUpdateDto;
import cn.zuo.entity.Discussion;
import cn.zuo.entity.Reply;
import cn.zuo.exception.discussionException.DiscussionDeleteException;
import cn.zuo.exception.discussionException.DiscussionQueryException;
import cn.zuo.mapper.DiscussionMapper;
import cn.zuo.mapper.ReplyMapper;
import cn.zuo.result.PageResult;
import cn.zuo.service.DiscussionService;
import cn.zuo.utils.ThreadLocalUtil;
import cn.zuo.vo.admin.CategoryStatVo;
import cn.zuo.vo.discussionvo.DiscussionDetailVo;
import cn.zuo.vo.discussionvo.DiscussionMessageVo;
import cn.zuo.vo.discussionvo.HotTitleVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DiscussionServiceImpl extends ServiceImpl<DiscussionMapper, Discussion> implements DiscussionService {

    @Resource
    private DiscussionMapper discussionMapper;

    @Resource
    private ReplyMapper replyMapper;

    /**
     * 分页查询讨论列表
     * @param discussionPageQueryDto
     * @return
     */
    @Override
    public PageResult pageQueryDiscussions(DiscussionPageQueryDto discussionPageQueryDto){
        // 创建分页对象
        Page<Discussion> pageInfo = new Page<>(discussionPageQueryDto.getPage(), discussionPageQueryDto.getSize());

        // 创建查询条件构造器
        QueryWrapper<Discussion> wrapper = new QueryWrapper<>();

        // 分类筛选
        if (discussionPageQueryDto.getCategory() != null && !"all".equals(discussionPageQueryDto.getCategory())) {
            wrapper.eq("category", discussionPageQueryDto.getCategory());
        }

        // 标签筛选
        if (discussionPageQueryDto.getTags() != null && !discussionPageQueryDto.getTags().isEmpty()) {
            wrapper.like("tags", discussionPageQueryDto.getTags());
        }

        // 关键词搜索（标题或内容）
        if (discussionPageQueryDto.getKeyword() != null && !discussionPageQueryDto.getKeyword().isEmpty()) {
            wrapper.and(w -> w.like("title", discussionPageQueryDto.getKeyword())
                    .or()
                    .like("content", discussionPageQueryDto.getKeyword()));
        }

        // 只查询活跃状态的讨论
        wrapper.eq("status", "active");

        // 按创建时间倒序排列
        wrapper.orderByDesc("created_time");

        // 执行分页查询
        Page<Discussion> discussionPage = discussionMapper.selectPage(pageInfo, wrapper);
        return new PageResult(discussionPage.getTotal(), discussionPage.getRecords());
    }

    /**
     * 根据讨论ID查询讨论详情
     * @param discussionId
     * @return
     */
    @Override
    public DiscussionDetailVo getDiscussionDetailById(Long discussionId){
        Discussion discussion = discussionMapper.selectById(discussionId);
        if (discussion == null) {
            throw new DiscussionQueryException("讨论不存在");
        }
        // 增加浏览量
        discussion.setViewCount(discussion.getViewCount() + 1);
        discussionMapper.updateById(discussion);
        // 获取回复列表
        QueryWrapper<Reply> replyWrapper = new QueryWrapper<>();
        replyWrapper.eq("discussion_id", discussionId)
                .eq("reply_layer", 0) // 只获取顶级回复
                .eq("status", "active")
                .orderByAsc("created_time");
        List<Reply> replies = replyMapper.selectList(replyWrapper);
        // 转换为VO
        DiscussionDetailVo discussionDetailVo = new DiscussionDetailVo();
        BeanUtils.copyProperties(discussion, discussionDetailVo);
        discussionDetailVo.setReplies(replies);
        return discussionDetailVo;
    }

    @Override
    public List<Discussion> getAllDiscussions() {
        return discussionMapper.selectList(null);
    }

    @Override
    public Discussion getDiscussionById(Long discussionId) {
        return discussionMapper.selectById(discussionId);
    }

    @Override
    public List<Discussion> getDiscussionsByCourseId(Long courseId) {
        QueryWrapper<Discussion> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        return discussionMapper.selectList(wrapper);
    }

    @Override
    public List<Discussion> getDiscussionsByUserId(Long userId) {
        QueryWrapper<Discussion> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return discussionMapper.selectList(wrapper);
    }

    @Override
    public DiscussionMessageVo createDiscussion(DiscussionDto discussionDto) {
        // 设置默认值
        Discussion discussion = new Discussion();
        BeanUtils.copyProperties(discussionDto, discussion);
        discussion.setUserId(ThreadLocalUtil.getCurrentId());
        discussion.setStatus("active");
        discussion.setViewCount(0);
        discussion.setReplyCount(0);
        discussion.setLikeCount(0);
        discussion.setFavoriteCount(0);
        discussion.setCreatedTime(LocalDateTime.now());
        discussion.setUpdatedTime(LocalDateTime.now());
        discussionMapper.insert(discussion);
        // 转换为VO
        DiscussionMessageVo discussionMessageVo = new DiscussionMessageVo();
        discussionMessageVo.setDiscussionId(discussion.getId());
        discussionMessageVo.setMessage("讨论创建成功");
        return discussionMessageVo;
    }

    @Override
    public void updateDiscussion(DiscussionUpdateDto discussionUpdateDto) {
        Discussion discussion = discussionMapper.selectById(discussionUpdateDto.getId());
        if (discussion == null) {
            throw new DiscussionQueryException("讨论不存在");
        }
        BeanUtils.copyProperties(discussionUpdateDto, discussion);
        discussion.setUpdatedTime(LocalDateTime.now());
        discussionMapper.updateById(discussion);
    }

    @Override
    public void userDeleteDiscussion(Long discussionId) {
        Discussion discussion = discussionMapper.selectById(discussionId);
        if (discussion == null) {
            throw new DiscussionDeleteException("讨论不存在");
        }
        // 检查用户是否有权限删除
        if (!discussion.getUserId().equals(ThreadLocalUtil.getCurrentId())) {
            throw new DiscussionDeleteException("您没有权限删除其他用户的讨论");
        }
        // 软删除：修改状态为deleted
        discussion.setStatus("deleted");
        discussion.setUpdatedTime(LocalDateTime.now());
        discussionMapper.updateById(discussion);
    }

    @Override
    public void adminDeleteDiscussion(Long discussionId) {
        Discussion discussion = discussionMapper.selectById(discussionId);
        if (discussion == null) {
            throw new DiscussionDeleteException("讨论不存在");
        }
        // 软删除：修改状态为deleted
        discussion.setStatus("deleted");
        discussion.setUpdatedTime(LocalDateTime.now());
        discussionMapper.updateById(discussion);
    }

    @Override
    public Long getTotalDiscussions() {
        return discussionMapper.selectCount(null);
    }

    /**
     * 根据用户查询讨论
     * @param discussionPageQueryByUserDto
     * @return
     */
    @Override
    public PageResult getUserDiscussions(DiscussionPageQueryByUserDto discussionPageQueryByUserDto) {
        Page<Discussion> pageInfo = new Page<>(discussionPageQueryByUserDto.getPage(), discussionPageQueryByUserDto.getSize());
        QueryWrapper<Discussion> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", discussionPageQueryByUserDto.getUserId())
                .ne("status", "deleted")
                .orderByDesc("created_time");

        Page<Discussion> discussionPage = discussionMapper.selectPage(pageInfo, wrapper);
        return new PageResult(discussionPage.getTotal(), discussionPage.getRecords());
    }

    /**
     * 根据讨论ID关闭讨论帖
     * @param discussionId
     */
    @Override
    public void closeDiscussion(Long discussionId) {
        Discussion discussion = discussionMapper.selectById(discussionId);
        if (discussion == null) {
            throw new DiscussionQueryException("讨论不存在");
        }
        discussion.setStatus("closed");
        discussion.setUpdatedTime(LocalDateTime.now());
        discussionMapper.updateById(discussion);
    }

    /**
     * 获取热门讨论贴
     * @param discussionPageQueryDto
     * @return
     */
    @Override
    public PageResult getHotDiscussions(DiscussionPageQueryDto discussionPageQueryDto) {
        Page<Discussion> pageInfo = new Page<>(discussionPageQueryDto.getPage(), discussionPageQueryDto.getSize());
        QueryWrapper<Discussion> wrapper = new QueryWrapper<>();
        // 分类筛选
        if (discussionPageQueryDto.getCategory() != null && !"all".equals(discussionPageQueryDto.getCategory())) {
            wrapper.eq("category", discussionPageQueryDto.getCategory());
        }

        // 标签筛选
        if (discussionPageQueryDto.getTags() != null && !discussionPageQueryDto.getTags().isEmpty()) {
            wrapper.like("tags", discussionPageQueryDto.getTags());
        }

        // 关键词搜索（标题或内容）
        if (discussionPageQueryDto.getKeyword() != null && !discussionPageQueryDto.getKeyword().isEmpty()) {
            wrapper.and(w -> w.like("title", discussionPageQueryDto.getKeyword())
                    .or()
                    .like("content", discussionPageQueryDto.getKeyword()));
        }

        wrapper.eq("status", "active")
                .orderByDesc("view_count + reply_count * 2");

        Page<Discussion> hotDiscussions = discussionMapper.selectPage(pageInfo, wrapper);
        return new PageResult(hotDiscussions.getTotal(), hotDiscussions.getRecords());
    }

    /**
     * 获取热门标题
     * @return
     */
    @Override
    public PageResult<HotTitleVo> getHotTitles() {
        Page<Discussion> pageInfo = new Page(1, 10);
        QueryWrapper<Discussion> queryWrapper = new QueryWrapper<>();
        List<HotTitleVo> hotTitleVos = new ArrayList<>();
        queryWrapper.eq("status", "active")
                .orderByDesc("view_count + reply_count * 2");
        Page<Discussion> hotTitles = discussionMapper.selectPage(pageInfo, queryWrapper);
        hotTitles.getRecords().forEach(discussion -> {
            HotTitleVo hotTitleVo = new HotTitleVo();
            hotTitleVo.setTitle(discussion.getTitle());
            hotTitleVo.setDiscussionId(discussion.getId());
            hotTitleVos.add(hotTitleVo);
        });
        return new PageResult<>(hotTitles.getTotal(), hotTitleVos);
    }



    @Override
    public List<CategoryStatVo> getCategoryStats() {
        return discussionMapper.countByCategory();
    }

    @Override
    public boolean incrementViewCount(Long discussionId) {
        Discussion discussion = discussionMapper.selectById(discussionId);
        if (discussion != null) {
            discussion.setViewCount(discussion.getViewCount() + 1);
            int result = discussionMapper.updateById(discussion);
            return result > 0;
        }
        return false;
    }
}