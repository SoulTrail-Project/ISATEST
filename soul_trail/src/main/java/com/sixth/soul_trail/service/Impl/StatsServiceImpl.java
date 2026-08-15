package com.sixth.soul_trail.service.Impl;

import com.sixth.soul_trail.VO.TrendVo;
import com.sixth.soul_trail.VO.DiaryStatisticsData;
import com.sixth.soul_trail.processor.SummaryStatsProcesser;
import com.sixth.soul_trail.processor.TrendStatsProcessor;
import com.sixth.soul_trail.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StatsServiceImpl implements StatsService{

    @Autowired
    private SummaryStatsProcesser summaryStatsProcesser;

    @Autowired
    private TrendStatsProcessor trendStatsProcessor;

    @Override
    public DiaryStatisticsData getSummaryStats(Long userId) {
        return summaryStatsProcesser.getSummaryStats(userId);
    }

    @Override
    public List<TrendVo> getTrendStats(Long userId,int range) {
        return trendStatsProcessor.getTrendStats(userId,range);
    }

}
