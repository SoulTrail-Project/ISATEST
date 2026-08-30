package com.sixth.soul_trail.VO;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class DiaryExportVO {
    // 这个类是用来输出excel的

    @ExcelProperty(value = "日记ID", index = 0)
    private Long diaryId;

    @ExcelProperty(value = "日记内容", index = 1)
    private String content;

    @ExcelProperty(value = "情绪", index = 2)
    private String moodType;

    @ExcelProperty(value = "创建时间", index = 3)
    private String createdAt;

}
