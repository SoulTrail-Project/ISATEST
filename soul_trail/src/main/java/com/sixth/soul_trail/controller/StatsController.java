package com.sixth.soul_trail.controller;

import com.sixth.soul_trail.VO.DiaryStatisticsData;
import com.sixth.soul_trail.common.Result;
import com.sixth.soul_trail.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    private StatsService statsService;

    @PostMapping("/summary")
    public Result<DiaryStatisticsData> getStatistics(@RequestParam Long userId) {
        DiaryStatisticsData data = statsService.getSummaryStats(userId);
        return Result.success(data);
    }

}
