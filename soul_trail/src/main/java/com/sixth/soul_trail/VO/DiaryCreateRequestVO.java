package com.sixth.soul_trail.VO;

import lombok.Data;

@Data
public class DiaryCreateRequestVO {
    private String title;
    private String content;
    private String moodType;
    private String sentimentEmotion;
}