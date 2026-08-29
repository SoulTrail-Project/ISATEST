package com.sixth.soul_trail.VO;

import lombok.Data;

import java.util.List;

@Data
public class DiaryUpdateRequestVO {
    private String title;
    private String content;
    private String moodType;
    private List<String> tags;
}