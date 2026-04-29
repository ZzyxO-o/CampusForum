package cn.zuo.service.impl;

import cn.zuo.dto.favoritedto.FavoriteDto;
import cn.zuo.dto.favoritedto.FavoritePageQueryDto;
import cn.zuo.entity.Discussion;
import cn.zuo.entity.Favorite;
import cn.zuo.entity.Notification;
import cn.zuo.entity.User;
import cn.zuo.exception.BusinessException;
import cn.zuo.mapper.DiscussionMapper;
import cn.zuo.mapper.FavoriteMapper;
import cn.zuo.mapper.NotificationMapper;
import cn.zuo.mapper.UserMapper;
import cn.zuo.result.PageResult;
import cn.zuo.service.FavoriteService;
import cn.zuo.utils.ThreadLocalUtil;
import cn.zuo.vo.favoritevo.FavoriteMessageVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {

    @Resource
    private FavoriteMapper favoriteMapper;

    @Resource
    private DiscussionMapper discussionMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private NotificationMapper notificationMapper;

    @Override
    @Transactional
    public FavoriteMessageVo toggleFavorite(FavoriteDto favoriteDto) {
        Long userId = favoriteDto.getUserId();
        //用户是否是当前用户
        if (!userId.equals(ThreadLocalUtil.getCurrentId())) {
            throw new BusinessException("无法操作其他用户的收藏");
        }

        Long discussionId = favoriteDto.getDiscussionId();

        // 查询是否已存在收藏记录
        QueryWrapper<Favorite> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .eq("discussion_id", discussionId);

        Favorite existingFavorite = this.getOne(wrapper);

        boolean isFavorited;
        String message;

        if (existingFavorite != null) {
            // 已存在记录，切换状态
            if (existingFavorite.getStatus() == 1) {
                // 当前是收藏状态，取消收藏
                existingFavorite.setStatus(0);
                this.updateById(existingFavorite);
                favoriteMapper.decrementFavoriteCount(discussionId);
                isFavorited = false;
                message = "取消收藏";
            } else {
                // 当前是取消状态，恢复收藏
                existingFavorite.setStatus(1);
                this.updateById(existingFavorite);
                favoriteMapper.incrementFavoriteCount(discussionId);
                isFavorited = true;
                message = "收藏成功";
            }
        } else {
            // 不存在记录，创建新收藏
            Favorite newFavorite = new Favorite();
            newFavorite.setUserId(userId);
            newFavorite.setDiscussionId(discussionId);
            newFavorite.setStatus(1);
            newFavorite.setCreatedTime(LocalDateTime.now());
            this.save(newFavorite);
            favoriteMapper.incrementFavoriteCount(discussionId);
            isFavorited = true;
            message = "收藏成功";
            // 查询用户信息
            User user = userMapper.selectById(userId);

            // 查询讨论信息
            Discussion discussion = discussionMapper.selectById(discussionId);

            // 创建通知
            Notification notification = new Notification();
            notification.setUserId(discussion.getUserId());
            notification.setRelatedId(discussionId);
            notification.setType("favorite");
            notification.setSenderId(userId);
            notification.setTitle("收藏通知");
            notification.setContent(user.getUsername() + "收藏了你的帖子" + discussion.getTitle());
            notification.setIsRead(Boolean.FALSE);
            notification.setCreatedTime(LocalDateTime.now());
            notificationMapper.insert(notification);
        }

        // 构建返回结果
        FavoriteMessageVo favoriteVo = new FavoriteMessageVo();
        favoriteVo.setStatus(isFavorited ? 1 : 0);
        favoriteVo.setFavoriteCount(getFavoriteCount(discussionId));
        favoriteVo.setMessage(message);
        return favoriteVo;
    }

    @Override
    public boolean isFavorited(Long discussionId) {
        Long userId = ThreadLocalUtil.getCurrentId();
        QueryWrapper<Favorite> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .eq("discussion_id", discussionId)
                .eq("status", 1);
        return this.count(wrapper) > 0;
    }

    @Override
    public PageResult<Favorite> getUserFavorites(FavoritePageQueryDto favoritePageQueryDto) {
        Long userId = favoritePageQueryDto.getUserId();
        //用户是否是当前用户
        if (!userId.equals(ThreadLocalUtil.getCurrentId())) {
            throw new BusinessException("无法操作其他用户的收藏");
        }
        // 获取用户收藏的讨论ID列表
        Page<Favorite> page = new Page<>(favoritePageQueryDto.getPage(), favoritePageQueryDto.getSize());
        QueryWrapper<Favorite> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .eq("status", 1);
        Page<Favorite> favoritePage = favoriteMapper.selectPage(page, wrapper);
        return new PageResult<>(favoritePage.getTotal(), favoritePage.getRecords());
    }

    @Override
    public int getFavoriteCount(Long discussionId) {
        QueryWrapper<Favorite> wrapper = new QueryWrapper<>();
        wrapper.eq("discussion_id", discussionId)
                .eq("status", 1);
        return Math.toIntExact(this.count(wrapper));
    }
}