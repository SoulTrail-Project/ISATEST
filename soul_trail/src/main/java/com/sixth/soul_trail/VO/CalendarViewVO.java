package com.sixth.soul_trail.VO;

import lombok.Data;
import java.util.List;

/**
 * 接口6完整返回VO
 * 作用：日历页面一打开，所有需要的数据都在这里了
 */
@Data
public class CalendarViewVO {
    private MonthlyStatsVO stats;                  // 月度统计（顶部报告）
    private List<CalendarDayVO> days;              // 每天的数据（日历格子用）
    private List<String> topKeywords;              // 当月热词Top10（词云用）
    private MoodThemeVO monthMood;                 // 本月主心情主题（整个月的皮肤）
    private List<MoodDistributionVO> moodDistribution;  // 心情分布（饼图用）
    private String monthQuote;                     // 本月专属语录（底部暖心话）
}
