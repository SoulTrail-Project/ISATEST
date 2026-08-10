package com.sixth.soul_trail.service.Impl;


import com.sixth.soul_trail.VO.*;
import com.sixth.soul_trail.common.MoodTypeEnum;
import com.sixth.soul_trail.exception.BusinessException;
import com.sixth.soul_trail.mapper.DiaryMapper;
import com.sixth.soul_trail.service.DiaryStatsService;
import com.sixth.soul_trail.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 日记统计服务实现类
 * 核心逻辑都在这里
 */
@Service
public class DiaryStatsServiceImpl implements DiaryStatsService {

    @Autowired
    private DiaryMapper diaryMapper;

    // ================================================================
    // 接口6：日历视图（增强版）
    // ================================================================
    @Override
    public CalendarViewVO getCalendarView(Integer year, Integer month) {
        // 1. 从token里拿当前登录用户的ID
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(401,"请先登录");
        }

        // 2. 计算这个月的第一天和最后一天
        YearMonth ym = YearMonth.of(year, month);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        // 3. 查数据库：按天聚合的统计数据
        List<Map<String, Object>> dailyStats = diaryMapper.selectDailyStats(userId, start, end);

        // 4. 把数据转成Map，方便后面按日期查找
        Map<LocalDate, Map<String, Object>> statsMap = new HashMap<>();
        for (Map<String, Object> row : dailyStats) {
            LocalDate date = ((java.sql.Date) row.get("diary_date")).toLocalDate();
            statsMap.put(date, row);
        }

        // 5. 组装每天的日历数据 + 心情主题
        List<CalendarDayVO> days = new ArrayList<>();
        for (Map<String, Object> row : dailyStats) {
            LocalDate date = ((java.sql.Date) row.get("diary_date")).toLocalDate();

            CalendarDayVO dayVO = new CalendarDayVO();
            dayVO.setDate(date.toString());
            dayVO.setAvgScore(((Number) row.get("avg_score")).doubleValue());
            dayVO.setCount(((Number) row.get("diary_count")).intValue());

            String moodCode = (String) row.get("main_mood");
            dayVO.setMainMood(moodCode);
            dayVO.setMoodTheme(buildMoodTheme(moodCode));  // 挂上心情主题

            days.add(dayVO);
        }

        // 6. 计算月度统计数据
        MonthlyStatsVO stats = calcMonthlyStats(ym, statsMap);

        // 7. 查当月热词Top10
        List<String> topKeywords = diaryMapper.selectTopKeywords(userId, start, end, 10);

        // 8. 计算心情分布（饼图数据）
        List<MoodDistributionVO> distribution = calcMoodDistribution(statsMap);

        // 9. 算本月主心情 + 生成月度语录
        MoodTypeEnum monthMood = MoodTypeEnum.getByScore(stats.getAvgEmotion());
        String monthQuote = generateMonthQuote(stats);

        // 10. 组装最终返回结果
        CalendarViewVO result = new CalendarViewVO();
        result.setStats(stats);
        result.setDays(days);
        result.setTopKeywords(topKeywords);
        result.setMonthMood(buildMoodTheme(monthMood.getCode()));
        result.setMoodDistribution(distribution);
        result.setMonthQuote(monthQuote);

        return result;
    }

    // ================================================================
    // 接口12：日历热力图
    // ================================================================
    @Override
    public List<HeatmapDayVO> getHeatmap(String monthStr) {
        // 1. 拿用户ID
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(401,"请先登录");
        }

        // 2. 解析月份字符串，比如 "2025-08"
        YearMonth ym = YearMonth.parse(monthStr);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        // 3. 查聚合数据
        List<Map<String, Object>> dailyStats = diaryMapper.selectDailyStats(userId, start, end);
        Map<LocalDate, Map<String, Object>> statsMap = new HashMap<>();
        for (Map<String, Object> row : dailyStats) {
            LocalDate date = ((java.sql.Date) row.get("diary_date")).toLocalDate();
            statsMap.put(date, row);
        }

        // 4. 组装整月每一天的数据（没写日记的日子也返回，score为null）
        List<HeatmapDayVO> result = new ArrayList<>();
        for (int day = 1; day <= ym.lengthOfMonth(); day++) {
            LocalDate date = ym.atDay(day);
            HeatmapDayVO vo = new HeatmapDayVO();
            vo.setDate(date.toString());

            Map<String, Object> row = statsMap.get(date);
            if (row == null) {
                // 这天没写日记
                vo.setScore(null);
                vo.setCount(0);
            } else {
                // 这天有日记
                vo.setScore(((Number) row.get("avg_score")).doubleValue());
                vo.setCount(((Number) row.get("diary_count")).intValue());
            }
            result.add(vo);
        }

        return result;
    }

    // ================================================================
    // 获取所有心情类型（给选择器用）
    // ================================================================
    @Override
    public List<MoodThemeVO> getAllMoodTypes() {
        List<MoodThemeVO> list = new ArrayList<>();
        for (MoodTypeEnum mood : MoodTypeEnum.values()) {
            list.add(buildMoodTheme(mood.getCode()));
        }
        return list;
    }

    // ================================================================
    // 下面都是工具方法（private，外部调不到）
    // ================================================================

    /**
     * 根据心情编码构建心情主题VO
     * 把枚举里的信息拷贝到VO里返回给前端
     */
    private MoodThemeVO buildMoodTheme(String moodCode) {
        MoodTypeEnum mood = MoodTypeEnum.getByCode(moodCode);
        MoodThemeVO vo = new MoodThemeVO();
        vo.setCode(mood.getCode());
        vo.setName(mood.getName());
        vo.setBgImage(mood.getBgImage());
        vo.setThemeColor(mood.getThemeColor());
        vo.setEmoji(mood.getEmoji());
        vo.setQuote(mood.getQuote());
        vo.setSuggestion(mood.getSuggestion());
        return vo;
    }

    /**
     * 计算月度统计数据
     * 包括：总天数、写日记天数、打卡率、连续打卡、心情分布天数、平均分
     */
    private MonthlyStatsVO calcMonthlyStats(YearMonth ym, Map<LocalDate, Map<String, Object>> statsMap) {
        MonthlyStatsVO stats = new MonthlyStatsVO();
        int totalDays = ym.lengthOfMonth();

        stats.setTotalDays(totalDays);
        stats.setDiaryDays(statsMap.size());
        // 打卡率 = 写日记天数 / 总天数，保留两位小数
        stats.setDiaryRate(Math.round(statsMap.size() * 100.0 / totalDays) / 100.0);

        // 统计积极/中性/消极天数 + 算总分
        int positive = 0, neutral = 0, negative = 0;
        double totalScore = 0;

        for (Map<String, Object> row : statsMap.values()) {
            double score = ((Number) row.get("avg_score")).doubleValue();
            totalScore += score;
            if (score >= 0.6) positive++;           // >=0.6 = 积极
            else if (score >= 0.4) neutral++;       // 0.4~0.6 = 中性
            else negative++;                         // <0.4 = 消极
        }

        stats.setPositiveDays(positive);
        stats.setNeutralDays(neutral);
        stats.setNegativeDays(negative);

        // 月均情感分
        if (statsMap.isEmpty()) {
            stats.setAvgEmotion(0.0);
        } else {
            double avg = totalScore / statsMap.size();
            stats.setAvgEmotion(Math.round(avg * 100.0) / 100.0);
        }

        // 连续打卡天数（从月底往前数，遇到没写的那天就停）
        int streak = 0;
        for (int day = totalDays; day >= 1; day--) {
            if (statsMap.containsKey(ym.atDay(day))) {
                streak++;
            } else {
                break;
            }
        }
        stats.setStreakDays(streak);

        return stats;
    }

    /**
     * 计算心情分布（饼图数据）
     * 统计每种心情各出现了几天，算占比
     */
    private List<MoodDistributionVO> calcMoodDistribution(Map<LocalDate, Map<String, Object>> statsMap) {
        // 先数每种心情有几天
        Map<String, Integer> countMap = new HashMap<>();
        for (Map<String, Object> row : statsMap.values()) {
            String mood = (String) row.get("main_mood");
            countMap.merge(mood, 1, Integer::sum);
        }

        int total = statsMap.size();
        List<MoodDistributionVO> list = new ArrayList<>();

        // 转成VO列表
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            MoodTypeEnum mood = MoodTypeEnum.getByCode(entry.getKey());
            MoodDistributionVO vo = new MoodDistributionVO();
            vo.setMoodCode(entry.getKey());
            vo.setMoodName(mood.getName());
            vo.setCount(entry.getValue());
            // 占比 = 该心情天数 / 总天数 * 100，保留一位小数
            double pct = entry.getValue() * 100.0 / total;
            vo.setPercentage(Math.round(pct * 10.0) / 10.0);
            vo.setThemeColor(mood.getThemeColor());
            vo.setEmoji(mood.getEmoji());
            list.add(vo);
        }

        // 按天数从多到少排序
        list.sort((a, b) -> b.getCount() - a.getCount());
        return list;
    }

    /**
     * 生成本月专属语录
     * 根据这个月的心情情况，说一句合适的话
     */
    private String generateMonthQuote(MonthlyStatsVO stats) {
        double avg = stats.getAvgEmotion();

        if (stats.getDiaryDays() == 0) {
            return "这个月还没写日记呢，从今天开始吧~";
        }
        if (avg >= 0.7) {
            return String.format("这个月超棒！平均心情%.0f分，继续保持这份快乐~", avg * 100);
        }
        if (avg >= 0.5) {
            return "平平淡淡才是真，这个月也辛苦啦";
        }
        return "这个月可能有点难，但你已经很棒了，下个月会更好的";
    }
}
