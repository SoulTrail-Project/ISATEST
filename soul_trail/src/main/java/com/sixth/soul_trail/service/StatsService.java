package com.sixth.soul_trail.service;

import com.sixth.soul_trail.VO.DiaryStatisticsData;
import com.sixth.soul_trail.VO.TrendVo;
import com.sixth.soul_trail.VO.EmotionDistributionVO;
import com.sixth.soul_trail.VO.WordCloudVO;

import java.util.List;
import java.util.Map;

public interface StatsService {
    // interface_8
    DiaryStatisticsData getSummaryStats(Long userId);
    // interface_9
    List<TrendVo> getTrendStats(Long userId, int range);
    // interface_10
    List<EmotionDistributionVO> getEmotionDistribution(Long userId);
    // interface_11
    List<WordCloudVO> getWordCloud(Long userId);
    // interface_21 情绪频率, 按时间维度统计(7/30/all)，只统计当前登录用户
    Map<String,Long> getEmotionalFrequency(Long userId, String days);
}
