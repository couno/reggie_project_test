package com.couno.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.couno.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Couno
 * @date 2022/11/22 9:46
 * @Description
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
