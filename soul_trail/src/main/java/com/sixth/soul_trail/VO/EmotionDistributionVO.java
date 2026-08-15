package com.sixth.soul_trail.VO;

import com.sixth.soul_trail.service.DiaryService;
import lombok.Data;

@Data
public class EmotionDistributionVO {
    private String name;
    private Long value;
    private String moodType;
    private String color;
}
