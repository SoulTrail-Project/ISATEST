package com.sixth.soul_trail.mapper;

import com.sixth.soul_trail.pojo.Diary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;     // ← 新加
import java.util.List;
import java.util.Map;          // ← 新加

@Mapper
public interface DiaryMapper {

    int insert(Diary diary);
    // ========== 你原来的6个方法，不动 ==========

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
     * 查询热词TopN（词云用，数据来源 diary_keyword 表，跟用户标签无关）
     */
    List<String> selectTopKeywords(@Param("userId") Long userId,
                                   @Param("start") LocalDate start,
                                   @Param("end") LocalDate end,
                                   @Param("limit") Integer limit);

    List<Diary> selectDiaryDate(@Param("diaryDate") LocalDate diaryDate,
                          @Param("userId") Long userId);

    /**
     * 本周高频标签：从 diary.tags（JSON 数组列）拆出每个标签并统计次数
     * 固定返回出现次数最多的 2 个（SQL 里 LIMIT 2），不开放条数参数
     *
     * @param startDate 起始日期（传本周一）
     * @param endDate   结束日期（传今天）
     */
    List<String> selectTopTagsByUserId(@Param("userId") Long userId,
                                       @Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate);

    List<String> selectEmotionalFrequency(@Param("userId") Long userId,
                                          @Param("days") int days);

    List<String> selectAllMoodType(@Param("userId") Long userId);

    List<Diary> selectKeyword(@Param("userId") Long userId,
                              @Param("keyword") String keyword,
                              @Param("offset") int offset,
                              @Param("pageSize") int pageSize);

    List<Diary> selectExportData(@Param("userId") Long userId);

    /**
     * 接口30：本周记录统计（本周一 00:00 至今）
     * @return key: recordDays(有日记的天数) / totalCount(日记总篇数)
     */
    Map<String, Object> selectWeeklyRecord(@Param("userId") Long userId);

    /**
     * 接口31：本周出现次数最多的情绪（并列时取最近写的一篇）
     * @return key: moodType(情绪码) / cnt(次数)；本周无日记时返回 null
     */
    Map<String, Object> selectWeeklyTopMood(@Param("userId") Long userId);

}