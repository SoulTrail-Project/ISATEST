package com.sixth.soul_trail.mapper;

import com.sixth.soul_trail.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public class UserMapper {

    // 新增用户
    int insert(User user);

}
