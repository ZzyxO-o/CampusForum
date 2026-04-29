package cn.zuo.mapper;

import cn.zuo.entity.Favorite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {

    /**
     * 增加讨论的收藏数
     */
    @Update("UPDATE discussions SET favorite_count = favorite_count + 1 WHERE id = #{discussionId}")
    int incrementFavoriteCount(@Param("discussionId") Long discussionId);

    /**
     * 减少讨论的收藏数
     */
    @Update("UPDATE discussions SET favorite_count = GREATEST(0, favorite_count - 1) WHERE id = #{discussionId}")
    int decrementFavoriteCount(@Param("discussionId") Long discussionId);
}
