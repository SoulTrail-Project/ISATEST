package com.sixth.soul_trail.VO;

import lombok.Data;

@Data
public class DiaryUpdateRequestVO {
    private String title;
    private String content;
    private String moodType;
}