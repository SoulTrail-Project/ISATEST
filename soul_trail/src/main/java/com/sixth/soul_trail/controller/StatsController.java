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
    public Result<DiaryStatisticsData> getSummaryStats(@RequestParam Long userId) {
        DiaryStatisticsData data = statsService.getSummaryStats(userId);
        return Result.success(data);
    }

    // interface_9
    @PostMapping("/trend")
    public Result<List<TrendVo>> getTrendStats(
            @RequestParam int range,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {

        if (authHeader == null || authHeader.isEmpty()) {
            return new Result<>(401, "未登录", null);
        }

        // 1. 从请求头获取 Token
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        if (token == null || token.isEmpty()) {
            return new Result<>(401, "未登录或Token已过期", null);
        }

        // 2. 去掉 "Bearer " 前缀（如果有）
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 3. 解析 Token 获取 userId
        Long userId = JwtUtil.parseToken(token);
        if (userId == null) {
            return new Result<>(401, "Token无效或已过期", null);
        }

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

    @GetMapping("/emotion-distribution")
    public Result<Map<String,Long>> getEmotionalFrequency (@RequestParam(defaultValue = "7") String days) {
        String[] daysArr = {"7","30","all"};
        if (!Arrays.asList(daysArr).contains(days)) {
            return Result.error(200,"只能查询三种情况: 7/30/all");
        }
        Map<String,Long> emotionalFrequencyList = statsService.getEmotionalFrequency(days);
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
