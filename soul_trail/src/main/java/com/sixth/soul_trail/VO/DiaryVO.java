package com.sixth.soul_trail.VO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DiaryVO {

    private Long id;

    private String title;

    private String content;

    private String moodType;

    private String sentimentEmotion;

    private String sentimentLabel;

    private float score;

    private String confidence;

    private String keywords;

    private List<String> tags;

    private LocalDateTime createdAt;

}
