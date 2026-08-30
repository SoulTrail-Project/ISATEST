package com.sixth.soul_trail.VO;

import lombok.Data;

/**
 * 接口31：本周出现次数最多的情绪
 * 口径：本周一 00:00 至今天，只统计当前登录用户、未删除的日记
 */
@Data
public class TopMoodVO {
    /** 情绪枚举码，如 happy */
    private String moodType;
    /** 情绪中文名，如 开心 */
    private String moodName;
    /** 情绪主题色，如 #FFD700（前端直接用） */
    private String color;
    /** 本周该情绪出现次数 */
    private Long count;
}
