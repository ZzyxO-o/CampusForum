package cn.zuo.mapper;

import cn.zuo.entity.User;
import cn.zuo.vo.uservo.ActiveUserVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    List<ActiveUserVo> selectActiveUsers(@Param("limit") Integer limit);
}