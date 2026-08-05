package com.sixth.soul_trail.pojo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Diary {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private Double emotionScore;
    private String emotionType;
    private String keywords;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}