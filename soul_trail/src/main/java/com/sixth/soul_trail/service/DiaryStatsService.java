package com.sixth.soul_trail.service;

import com.sixth.soul_trail.VO.CalendarViewVO;
import com.sixth.soul_trail.VO.HeatmapDayVO;
import com.sixth.soul_trail.VO.MoodThemeVO;
import java.util.List;

/**
 * 日记统计服务接口
 * 负责：日历视图、热力图、心情列表等统计类功能
 */
public interface DiaryStatsService {

    /**
     * 获取日历视图（接口6）
     * 包含：每天数据、月度统计、心情分布、热词、月度语录
     */
    CalendarViewVO getCalendarView(Integer year, Integer month);

    /**
     * 获取日历热力图（接口12）
     * 返回整月每一天的情感分数
     */
    List<HeatmapDayVO> getHeatmap(String month);

    /**
     * 获取所有心情类型列表
     * 给写日记页面的心情选择器用
     */
    List<MoodThemeVO> getAllMoodTypes();
}
