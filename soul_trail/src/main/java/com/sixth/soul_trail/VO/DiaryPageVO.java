package com.sixth.soul_trail.VO;

import lombok.Data;
import java.util.List;

@Data
public class DiaryPageVO {
    private List<DiaryVO> records;
    private long total;
    private int page;
    private int pageSize;
}