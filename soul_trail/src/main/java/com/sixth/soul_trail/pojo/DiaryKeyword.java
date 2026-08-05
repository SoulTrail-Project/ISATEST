package com.sixth.soul_trail.pojo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DiaryKeyword {
    private Long id;
    private Long diaryId;
    private Long userId;
    private String keyword;
    private LocalDateTime createdAt;
}