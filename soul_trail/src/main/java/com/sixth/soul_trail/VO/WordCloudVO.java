package com.sixth.soul_trail.VO;

import lombok.Data;

@Data
public class WordCloudVO {
    private String word; //关键词文本
    private Long value; //词频
}
