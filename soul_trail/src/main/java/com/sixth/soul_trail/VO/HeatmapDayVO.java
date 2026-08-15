package com.sixth.soul_trail.VO;

import lombok.Data;

/**
 * 热力图每天的数据VO
 * 对应接口12返回的数组里的每一项
 */
@Data
public class HeatmapDayVO {
    private String date;    // 日期 yyyy-MM-dd
    private Double score;   // 情感分（0~1），没写日记就是null
    private Integer count;  // 当天日记条数
}
