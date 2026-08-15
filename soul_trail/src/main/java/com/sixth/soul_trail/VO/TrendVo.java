package com.sixth.soul_trail.VO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TrendVo {
    private LocalDate date;
    private Double avgScore;
    private Integer diaryCount;
}
