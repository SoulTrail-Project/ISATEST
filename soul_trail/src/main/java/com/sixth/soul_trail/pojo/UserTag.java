package com.sixth.soul_trail.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserTag {
    private Long id;
    private Long userId;
    private String tagName;
    private Integer isPreset;
    private Integer sortOrder;
    private LocalDateTime createdAt;
}
