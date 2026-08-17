package com.sixth.soul_trail.VO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiaryStatisticsData {
    private int totalDiaryCount;
    private int activeDays;
    private SentTimeRatio sentTimeRatio;
    private double averageScore;
    private int streakDays;
    private int longestStreak;
    private CurrentMoodSummary currentMoodSummary;
}



