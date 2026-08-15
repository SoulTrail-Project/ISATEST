package com.sixth.soul_trail.VO;

import lombok.Data;

/**
 * 日历每天的数据VO
 * 对应接口6返回的days数组里的每一项
 */
@Data
public class CalendarDayVO {
    private String date;            // 日期，格式 yyyy-MM-dd
    private Double avgScore;        // 当天所有日记的平均情感分（0~1）
    private Integer count;          // 当天写了几条日记
    private String mainMood;        // 当天出现最多的心情编码
    private MoodThemeVO moodTheme;  // 心情主题（完整皮肤信息，前端直接用）
}
