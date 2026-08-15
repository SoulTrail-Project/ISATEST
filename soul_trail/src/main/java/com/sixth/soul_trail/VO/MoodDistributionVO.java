package com.sixth.soul_trail.VO;

import lombok.Data;

/**
 * 心情分布VO
 * 作用：给饼图/柱状图用，显示这个月每种心情各有几天
 */
@Data
public class MoodDistributionVO {
    private String moodCode;     // 心情编码
    private String moodName;     // 心情名称
    private Integer count;       // 出现了几天
    private Double percentage;   // 占比百分比，比如 30.5 就是30.5%
    private String themeColor;   // 颜色（饼图每块的颜色）
    private String emoji;        // 表情
}
