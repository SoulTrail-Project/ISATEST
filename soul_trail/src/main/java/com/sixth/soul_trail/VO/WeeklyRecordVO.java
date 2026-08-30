package com.sixth.soul_trail.VO;

import lombok.Data;

/**
 * 接口30：本周记录统计
 * 口径：本周一 00:00 至今天，只统计当前登录用户、未删除的日记
 */
@Data
public class WeeklyRecordVO {
    /** 本周有写日记的天数（按 diary_date 去重） */
    private Long recordDays;
    /** 本周日记总篇数 */
    private Long totalCount;
    /** 本周起始日期（周一），yyyy-MM-dd */
    private String weekStart;
    /** 本周结束日期（今天），yyyy-MM-dd */
    private String weekEnd;
}
