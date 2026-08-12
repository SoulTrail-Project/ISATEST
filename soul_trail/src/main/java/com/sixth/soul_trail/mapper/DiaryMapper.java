package com.sixth.soul_trail.mapper;

import com.sixth.soul_trail.pojo.Diary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DiaryMapper {

    int insert(Diary diary);

    List<Diary> selectPageByUserId(@Param("userId") Long userId,
                                   @Param("offset") int offset,
                                   @Param("pageSize") int pageSize);

    long countByUserId(@Param("userId") Long userId);

    Diary selectByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    int update(Diary diary);

    int softDeleteById(@Param("id") Long id, @Param("userId") Long userId);

    List<Map<String, Object>> countByEmotionType(Long userId);
}