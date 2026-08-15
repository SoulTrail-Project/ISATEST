package com.sixth.soul_trail.VO;

import lombok.Data;

/**
 * 月度统计VO
 * 作用：给日历页面顶部的"月度报告"用
 */
@Data
public class MonthlyStatsVO {
    private Integer totalDays;       // 当月总天数
    private Integer diaryDays;       // 写了日记的天数
    private Double diaryRate;        // 写日记比例（0~1），比如0.5就是半个月都写了
    private Integer streakDays;      // 连续打卡天数（从月底往前数）
    private Double avgEmotion;       // 月均情感分
    private Integer positiveDays;    // 心情好的天数（>=0.6）
    private Integer neutralDays;     // 心情一般的天数（0.4~0.6）
    private Integer negativeDays;    // 心情差的天数（<0.4）
}
