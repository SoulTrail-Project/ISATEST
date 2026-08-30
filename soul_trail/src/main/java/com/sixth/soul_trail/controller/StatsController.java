package com.sixth.soul_trail.controller;

import com.sixth.soul_trail.VO.*;
import com.sixth.soul_trail.common.Result;
import com.sixth.soul_trail.service.StatsService;
import com.sixth.soul_trail.utils.JwtUtil;
import com.sixth.soul_trail.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    private StatsService statsService;

    // interface_8
    @PostMapping("/summary")
    public Result<DiaryStatisticsData> getSummaryStats(){
        Long userId = SecurityUtil.getCurrentUserId();
        DiaryStatisticsData data = statsService.getSummaryStats(userId);
        return Result.success(data);
    }

    // interface_9
    @PostMapping("/trend")
    public Result<List<TrendVo>> getTrendStats(@RequestParam int range) {
        Long userId = SecurityUtil.getCurrentUserId();
        // 4. 校验 range 参数
        if (range != 7 && range != 30) {
            return new Result<>(400, "range 参数必须为 7 或 30", null);
        }

        // 5. 查询数据
        List<TrendVo> data = statsService.getTrendStats(userId, range);
        return Result.success(data);

    }

    // interface_10
    @GetMapping("/emotion-distribution")
    public Result<List<EmotionDistributionVO>> getEmotionDistribution() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return new Result<>(401, "未登录或Token已过期", null);
        }
        List<EmotionDistributionVO> data = statsService.getEmotionDistribution(userId);
        return Result.success(data);
    }

    // interface_21 情绪频率：与接口 10 同路径，靠 days 参数区分；只统计当前登录用户
    @GetMapping(value ="/emotion-distribution", params = "days")
    public Result<Map<String,Long>> getEmotionalFrequency (@RequestParam(defaultValue = "7") String days) {
        Long userId = SecurityUtil.getCurrentUserId();
        String[] daysArr = {"7","30","all"};
        if (!Arrays.asList(daysArr).contains(days)) {
            return Result.error(400,"只能查询三种情况: 7/30/all");
        }
        Map<String,Long> emotionalFrequencyList = statsService.getEmotionalFrequency(userId, days);
        return Result.success(emotionalFrequencyList);
    }

    // interface_11
    @GetMapping("/word-cloud")
    public Result<List<WordCloudVO>> getWordCloud() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return new Result<>(401, "未登录或Token已过期", null);
        }
        List<WordCloudVO> data = statsService.getWordCloud(userId);
        return Result.success(data);
    }






}
