package cn.zuo.service;

import cn.zuo.dto.likedto.LikeDto;
import cn.zuo.dto.likedto.LikePageQueryDto;
import cn.zuo.entity.Like;
import cn.zuo.result.PageResult;
import cn.zuo.vo.likevo.LikeMessageVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface LikeService extends IService<Like> {

    /**
     * 点赞/取消点赞
     * @param likeDto 点赞信息
     * @return 点赞结果（包含是否已点赞、点赞数、消息）
     */
    LikeMessageVo toggleLike(LikeDto likeDto);

    /**
     * 检查用户是否已点赞
     * @param likeDto 点赞信息
     * @return 是否已点赞
     */
    boolean isLiked(LikeDto likeDto);


    /**
     * 获取目标的点赞数
     * @param targetType 目标类型
     * @param targetId 目标ID
     * @return 点赞数
     */
    int getLikeCount(String targetType, Long targetId);

    /**
     * 获取用户点赞的列表
     * @param queryDto
     * @return
     */
    PageResult<Like> getUserLikes(LikePageQueryDto queryDto);
}