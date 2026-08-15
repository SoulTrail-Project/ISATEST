package com.sixth.soul_trail.mapper;

import com.sixth.soul_trail.pojo.Diary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;     // ← 新加
import java.util.List;
import java.util.Map;          // ← 新加

@Mapper
public interface DiaryMapper {

    // ========== 你原来的6个方法，不动 ==========
    int insert(Diary diary);

    List<Diary> selectPageByUserId(@Param("userId") Long userId,
                                   @Param("offset") int offset,
                                   @Param("pageSize") int pageSize);

    long countByUserId(@Param("userId") Long userId);

    Diary selectByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    int update(Diary diary);

    int softDeleteById(@Param("id") Long id, @Param("userId") Long userId);

    List<Map<String, Object>> countByEmotionType(Long userId);
    // ========== 新加这2个方法 ==========

    /**
     * 按天聚合统计日记数据
     */
    List<Map<String, Object>> selectDailyStats(@Param("userId") Long userId,
                                               @Param("start") LocalDate start,
                                               @Param("end") LocalDate end);

    /**
     * 查询热词TopN
     */
    List<String> selectTopKeywords(@Param("userId") Long userId,
                                   @Param("start") LocalDate start,
                                   @Param("end") LocalDate end,
                                   @Param("limit") Integer limit);
}