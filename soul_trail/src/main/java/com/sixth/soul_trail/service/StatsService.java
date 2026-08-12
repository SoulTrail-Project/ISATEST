package com.sixth.soul_trail.service;

import com.sixth.soul_trail.VO.DiaryStatisticsData;

public interface StatsService {
    DiaryStatisticsData getSummaryStats(Long userId);
}
