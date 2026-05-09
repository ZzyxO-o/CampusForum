package cn.zuo.mapper;

import cn.zuo.entity.Discussion;
import cn.zuo.vo.admin.CategoryStatVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DiscussionMapper extends BaseMapper<Discussion> {

    List<CategoryStatVo> countByCategory();
}