package com.sixth.soul_trail.VO;

import lombok.Data;

import java.util.List;

@Data
public class DiaryCreateRequestVO {
    private String title;
    private String content;
    private String moodType;
    private String sentimentEmotion;
    private List<String> tags;
}