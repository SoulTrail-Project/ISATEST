package com.sixth.soul_trail.mapper;

import com.sixth.soul_trail.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    // 新增用户
    int insert(User user);

    // 数据库中查找用户
    User selectByUsername(String username);

    User selectById(Long id);

    int updatePassword(@Param("id") Long id, @Param("password") String password);

    int updateNickname(@Param("id") Long id, @Param("nickname") String nickname);

    int updateAvatar(@Param("id") Long id, @Param("avatarUrl") String avatarUrl);
}
