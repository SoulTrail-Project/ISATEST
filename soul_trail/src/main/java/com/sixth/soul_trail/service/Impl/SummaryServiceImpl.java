package com.sixth.soul_trail.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sixth.soul_trail.VO.CurrentMoodSummary;
import com.sixth.soul_trail.VO.SentTimeRatio;
import com.sixth.soul_trail.mapper.StatsMapper;
import com.sixth.soul_trail.pojo.Diary;
import com.sixth.soul_trail.VO.DiaryStatisticsData;
import com.sixth.soul_trail.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SummaryServiceImpl implements StatsService{

    @Autowired
    private StatsMapper statsMapper;

    @Override
    public DiaryStatisticsData getSummaryStats(Long userId) {
        return new DiaryStatisticsData(
                getTotalDiaryCount(userId),
                getActiveDays(userId),
                getSentTimeRatio(userId),
                getAverageScore(userId),
                getStreakDays(userId),
                getLongestStreak(userId),
                getCurrentMoodSummary(userId)
        );
    }

    public int getTotalDiaryCount(Long userId) {
        LambdaQueryWrapper<Diary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Diary::getUserId,userId);
        return statsMapper.selectCount(wrapper).intValue();
    }

    public int getActiveDays(Long userId) {
        LambdaQueryWrapper<Diary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Diary::getUserId,userId)
                .select(Diary::getCreatedAt);
        List<Diary> diaries = statsMapper.selectList(wrapper);
        return diaries.stream()
                .map(Diary::getCreatedAt)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet())
                .size();
    }

    public SentTimeRatio getSentTimeRatio(Long userId) {
        QueryWrapper<Diary> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .groupBy("sentiment_lable")
                .select("sentiment_lable", "count(*) as count");
        List<Map<String, Object>> maps = statsMapper.selectMaps(wrapper);

        // 解析结果
        long positive = 0, neutral = 0, negative = 0;
        for (Map<String, Object> map : maps) {
            String sentiment = (String) map.get("sentiment");
            long count = (long) map.get("count");
            if ("positive".equals(sentiment)) positive = count;
            else if ("neutral".equals(sentiment)) neutral = count;
            else if ("negative".equals(sentiment)) negative = count;
        }
        long total = positive + neutral + negative;
        if (total == 0) return new SentTimeRatio(0.0, 0.0, 0.0);
        return new SentTimeRatio(
                (double) positive / total,
                (double) neutral / total,
                (double) negative / total
        );
    }

    public double getAverageScore(Long userId) {
        // MyBatis-Plus 查询单个值（这里返回平均分，可能为 null）
        QueryWrapper<Diary> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .select("score"); // 只查分数
        List<Double> scores = statsMapper.selectObjs(wrapper)
                .stream()
                .map(obj -> (Double) obj)
                .toList();
        return scores.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    // 私有辅助方法
    private List<LocalDate> getSortedDiaryDates(Long userId) {
        // 1. 创建查询条件
        LambdaQueryWrapper<Diary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Diary::getUserId, userId)
                .select(Diary::getDiaryDate)
                .orderByDesc(Diary::getDiaryDate);

        // 2. 查询出"残缺"的 Diary 对象列表（只有 diaryDate 字段有值）
        List<Diary> diaries = statsMapper.selectList(wrapper);

        // 3. 把每个 Diary 对象里的日期拽出来，丢掉为 null 的日期，最后装成一个 List<LocalDate> 返回
        return diaries.stream()
                .map(Diary::getDiaryDate)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public int getStreakDays(Long userId) {
        List<LocalDate> dates = getSortedDiaryDates(userId);
        if (dates.isEmpty()) return 0;

        // 从最新的日记日期开始往前推
        LocalDate today = LocalDate.now();
        LocalDate checkDate = dates.get(0); // 最新日期（已排序倒序）

        // 如果最新日记不是今天或昨天，连续中断（取决于业务规则，通常认为昨天也算连续）
        if (checkDate.isBefore(today.minusDays(1))) {
            return 0;
        }

        int streak = 0;
        LocalDate current = checkDate;
        for (LocalDate date : dates) {
            if (date.isEqual(current)) {
                streak++;
                current = current.minusDays(1); // 往前推一天
            } else if (date.isBefore(current)) {
                break; // 断更
            }
        }
        return streak;
    }

    public int getLongestStreak(Long userId) {
        List<LocalDate> dates = getSortedDiaryDates(userId);
        if (dates.isEmpty()) return 0;

        int maxStreak = 1;
        int currentStreak = 1;
        // 注意：dates 是倒序（从新到旧），为了方便计算连续，我们转为正序
        List<LocalDate> ascending = new ArrayList<>(dates);
        Collections.reverse(ascending);

        for (int i = 1; i < ascending.size(); i++) {
            LocalDate prev = ascending.get(i - 1);
            LocalDate curr = ascending.get(i);
            // 如果相差 1 天，连续
            if (curr.isEqual(prev.plusDays(1))) {
                currentStreak++;
                maxStreak = Math.max(maxStreak, currentStreak);
            } else {
                currentStreak = 1;
            }
        }
        return maxStreak;
    }

    public CurrentMoodSummary getCurrentMoodSummary(Long userId) {
        QueryWrapper<Diary> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .groupBy("sentiment")
                .select("sentiment", "count(*) as count")
                .orderByDesc("count")
                .last("limit 1"); // 取数量最多的第一条
        List<Map<String, Object>> maps = statsMapper.selectMaps(wrapper);
        if (maps.isEmpty()) {
            return new CurrentMoodSummary("unknown", 0);
        }
        Map<String, Object> top = maps.get(0);
        String mood = (String) top.get("sentiment");
        long count = (long) top.get("count");
        return new CurrentMoodSummary(mood, (int) count);
    }

}
