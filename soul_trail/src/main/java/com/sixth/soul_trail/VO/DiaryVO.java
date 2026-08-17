package com.sixth.soul_trail.VO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DiaryVO {
    private Long id;
    private String title;
    private String content;
    private Double emotionScore;
    private String emotionType;
    private String keywords;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}