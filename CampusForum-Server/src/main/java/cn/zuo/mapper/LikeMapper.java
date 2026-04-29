package cn.zuo.mapper;

import cn.zuo.entity.Like;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface LikeMapper extends BaseMapper<Like> {

    /**
     * 增加目标的点赞数
     */
    @Update("UPDATE ${tableName} SET like_count = like_count + 1 WHERE id = #{targetId}")
    int incrementLikeCount(@Param("tableName") String tableName, @Param("targetId") Long targetId);

    /**
     * 减少目标的点赞数
     */
    @Update("UPDATE ${tableName} SET like_count = GREATEST(0, like_count - 1) WHERE id = #{targetId}")
    int decrementLikeCount(@Param("tableName") String tableName, @Param("targetId") Long targetId);
}
