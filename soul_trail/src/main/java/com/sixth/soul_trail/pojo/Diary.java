package com.sixth.soul_trail.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("diary")
public class Diary {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String title;

    private String content;

    private String moodType;

    private String sentimentLable;

    private String sentimentEmotion;

    private String confidence;

    private String keywords;

    private String tags;

    @TableField("is_private")
    private boolean isPrivate;

    private LocalDate diaryDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer isDeleted;
}
