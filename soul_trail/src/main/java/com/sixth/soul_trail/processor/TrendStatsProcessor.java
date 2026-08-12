package com.sixth.soul_trail.processor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sixth.soul_trail.VO.TrendVo;
import com.sixth.soul_trail.mapper.StatsMapper;
import com.sixth.soul_trail.pojo.Diary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TrendStatsProcessor {

    @Autowired
    private StatsMapper statsMapper;

    public List<TrendVo> getTrendStats(Long userId, int range) {
        // 1. 日期范围
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(range - 1);

        // 2. 查询数据库按日期分组统计
        QueryWrapper<Diary> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .between("diary_date", startDate, endDate)
                .groupBy("diary_date")
                .select("diary_date as date",
                        "AVG(sentiment_score) as avgScore",
                        "COUNT(*) as diaryCount");

        List<Map<String, Object>> maps = statsMapper.selectMaps(wrapper);

        // 3. 转成 Map 便于补全
        Map<LocalDate, TrendVo> trendMap = new HashMap<>();
        for (Map<String, Object> map : maps) {
            LocalDate date = (LocalDate) map.get("date");
            Double avgScore = map.get("avgScore") != null ?
                    ((Number) map.get("avgScore")).doubleValue() : null;
            Integer diaryCount = ((Number) map.get("diaryCount")).intValue();
            trendMap.put(date, new TrendVo(date, avgScore, diaryCount));
        }

        // 4. 补全没有写日记的日期
        List<TrendVo> result = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            TrendVo trend = trendMap.get(date);
            if (trend == null) {
                trend = new TrendVo(date, null, 0);
            }
            result.add(trend);
        }
        return result;
    }

}
