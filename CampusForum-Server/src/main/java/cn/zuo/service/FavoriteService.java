package cn.zuo.service;

import cn.zuo.dto.favoritedto.FavoriteDto;
import cn.zuo.dto.favoritedto.FavoritePageQueryDto;
import cn.zuo.entity.Favorite;
import cn.zuo.result.PageResult;
import cn.zuo.vo.favoritevo.FavoriteMessageVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface FavoriteService extends IService<Favorite> {

    /**
     * 收藏/取消收藏
     * @param favoriteDto 收藏信息
     * @return 收藏结果（包含是否已收藏、收藏数、消息）
     */
    FavoriteMessageVo toggleFavorite(FavoriteDto favoriteDto);

    /**
     * 检查用户是否已收藏
     * @param discussionId 讨论ID
     * @return 是否已收藏
     */
    boolean isFavorited(Long discussionId);

    /**
     * 获取用户收藏的讨论列表
     * @return 讨论列表
     */
    PageResult<Favorite> getUserFavorites(FavoritePageQueryDto favoritePageQueryDto);

    /**
     * 获取讨论的收藏数
     * @param discussionId 讨论ID
     * @return 收藏数
     */
    int getFavoriteCount(Long discussionId);
}