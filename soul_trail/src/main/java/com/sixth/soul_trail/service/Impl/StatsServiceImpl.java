package com.sixth.soul_trail.service.Impl;

import com.sixth.soul_trail.VO.TrendVo;
import com.sixth.soul_trail.VO.DiaryStatisticsData;
import com.sixth.soul_trail.VO.EmotionDistributionVO;
import com.sixth.soul_trail.VO.WordCloudVO;
import com.sixth.soul_trail.common.MoodTypeEnum;
import com.sixth.soul_trail.mapper.DiaryMapper;
import com.sixth.soul_trail.mapper.DiaryKeywordMapper;
import com.sixth.soul_trail.processor.SummaryStatsProcesser;
import com.sixth.soul_trail.processor.TrendStatsProcessor;
import com.sixth.soul_trail.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatsServiceImpl implements StatsService{

    @Autowired
    private SummaryStatsProcesser summaryStatsProcesser;

    @Autowired
    private TrendStatsProcessor trendStatsProcessor;

    @Autowired
    private DiaryMapper diaryMapper;

    @Autowired
    private DiaryKeywordMapper diaryKeywordMapper;

    @Override
    public DiaryStatisticsData getSummaryStats(Long userId) {
        return summaryStatsProcesser.getSummaryStats(userId);
    }

    @Override
    public List<TrendVo> getTrendStats(Long userId,int range) {
        return trendStatsProcessor.getTrendStats(userId,range);
    }

    @Override
    public List<EmotionDistributionVO> getEmotionDistribution(Long userId) {
        List<Map<String, Object>> rows = diaryMapper.countByEmotionType(userId);
        List<EmotionDistributionVO> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            String moodType = (String) row.get("moodType");
            MoodTypeEnum mood = MoodTypeEnum.getByCode(moodType);
            EmotionDistributionVO vo = new EmotionDistributionVO();
            vo.setMoodType(moodType);
            vo.setName(mood != null ? mood.getName() : moodType);
            vo.setColor(mood != null ? mood.getThemeColor() : "#999999");
            vo.setValue(((Number) row.get("value")).longValue());
            result.add(vo);
        }
        return result;
    }

    @Override
    public List<WordCloudVO> getWordCloud(Long userId) {
        return diaryKeywordMapper.selectWordCloud(userId);
    }

    @Override
    public Map<String,Long> getEmotionalFrequency(String days) {
        List<String> emotionalFrequencyList;
        if (days.equals("all")) {
            emotionalFrequencyList = diaryMapper.selectAllMoodType();
        }
        else {
            emotionalFrequencyList = diaryMapper.selectEmotionalFrequency(Integer.parseInt(days));
        }
        return emotionalFrequencyList.stream()
                .collect(Collectors.groupingBy(String::toString,Collectors.counting()));
    }

}
