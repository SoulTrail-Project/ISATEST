package com.sixth.soul_trail.mapper;

import com.sixth.soul_trail.pojo.Summary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiaryMapper {

    int insert(Summary diary);

    List<Summary> selectPageByUserId(@Param("userId") Long userId,
                                     @Param("offset") int offset,
                                     @Param("pageSize") int pageSize);

    long countByUserId(@Param("userId") Long userId);

    Summary selectByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    int update(Summary diary);

    int softDeleteById(@Param("id") Long id, @Param("userId") Long userId);
}