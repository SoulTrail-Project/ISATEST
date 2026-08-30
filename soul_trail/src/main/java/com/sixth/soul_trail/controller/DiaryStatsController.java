package com.sixth.soul_trail.controller;

import com.alibaba.excel.EasyExcel;
import com.sixth.soul_trail.VO.CalendarViewVO;
import com.sixth.soul_trail.VO.DiaryExportVO;
import com.sixth.soul_trail.VO.HeatmapDayVO;
import com.sixth.soul_trail.VO.MoodThemeVO;
import com.sixth.soul_trail.common.Result;
import com.sixth.soul_trail.service.DiaryService;
import com.sixth.soul_trail.service.DiaryStatsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

/**
 * 日记统计控制器
 * 负责：日历视图、热力图、心情列表等统计接口
 */
@RestController
@RequestMapping("/api")
public class DiaryStatsController {

    @Autowired
    private DiaryStatsService diaryStatsService;
    @Autowired
    private DiaryService diaryService;

    /**
     * 接口6：日历视图（按天情感汇总）
     * 路径：GET /api/diaries/calendar
     * 参数：year（年）、month（月）
     * 返回：日历页面所有数据（每天数据+月度报告+心情分布+热词）
     */
    @GetMapping("/diaries/calendar")
    public Result<CalendarViewVO> getCalendar(
            @RequestParam Integer year,
            @RequestParam Integer month) {
        CalendarViewVO data = diaryStatsService.getCalendarView(year, month);
        return Result.success(data);
    }

    /**
     * 接口12：日历热力图
     * 路径：GET /api/stats/heatmap
     * 参数：month（月份，格式 yyyy-MM，如 "2025-08"）
     * 返回：整月每一天的情感分数（给热力图上色用）
     */
    @GetMapping("/stats/heatmap")
    public Result<List<HeatmapDayVO>> getHeatmap(@RequestParam String month) {
        List<HeatmapDayVO> data = diaryStatsService.getHeatmap(month);
        return Result.success(data);
    }

    /**
     * 新增：获取所有心情类型
     * 路径：GET /api/mood/types
     * 用途：写日记页面的心情选择器
     */
    @GetMapping("/mood/types")
    public Result<List<MoodThemeVO>> getAllMoodTypes() {
        List<MoodThemeVO> data = diaryStatsService.getAllMoodTypes();
        return Result.success(data);
    }

    // interface_29
    @PostMapping("/export/excel")
    public Result<String> exportExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("日记数据_" + LocalDate.now(), StandardCharsets.UTF_8).replaceAll("\\+","%20");
        response.setHeader("Content-Disposition","attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<DiaryExportVO> exportData = diaryService.getExportData();
        EasyExcel.write(response.getOutputStream(), DiaryExportVO.class)
                .sheet("日记列表")
                .doWrite(exportData);
        return Result.success("已导出excel, 请查看浏览器弹窗");
    }

}
