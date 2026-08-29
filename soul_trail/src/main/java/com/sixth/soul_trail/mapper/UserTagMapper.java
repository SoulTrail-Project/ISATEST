package com.sixth.soul_trail.mapper;

import com.sixth.soul_trail.pojo.UserTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserTagMapper {

    int insert(UserTag userTag);

    /**批量设置系统预设标签*/
    int insertBatch(@Param("userId") Long userId, @Param("tags") List<String> tags);

    List<String> selectTagNamesByUserId(@Param("userId") Long userId);

    int deleteByUserIdAngTagName(@Param("userId") Long userId,
                                 @Param("tagName") String tagName);

    int countByUserId(@Param("userId") Long userId);
}
