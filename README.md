### 委托模式设计
接口8和9各自都只有一个抽象方法。为集中管理，统一在StatsService中声明，\n
但直接把两个接口的实现放在一个实现类里，代码过多不便于开发和维护，\n
于是引入处理器(processor)，将实际的代码实现存入各自的处理器当中，实现了环境隔离
### 接口8
统计总览
接口返回详情：
```json
  {
  "code": 200,
  "message": "获取成功",
  "data": {
    "totalDiaryCount": 56,
    "activeDays": 30,
    "sentimentRatio": {
      "positive": 0.55,
      "neutral": 0.25,
      "negative": 0.20
    },
    "averageScore": 0.62,
    "streakDays": 5,
    "longestStreak": 12,
    "currentMoodSummary": {
      "mostFrequentMood": "happy",
      "mostFrequentMoodCount": 18
    }
  }
}
```
### 接口9
情绪趋势
（折线图）
接口返回详情：
```json
  {
  "code": 200,
  "message": "获取成功",
  "data": [
    { "date": "2025-07-26", "avgScore": 0.45, "diaryCount": 2 },
    { "date": "2025-07-27", "avgScore": 0.62, "diaryCount": 1 },
    { "date": "2025-07-28", "avgScore": 0.30, "diaryCount": 3 },
    { "date": "2025-07-29", "avgScore": 0.81, "diaryCount": 2 },
    { "date": "2025-07-30", "avgScore": 0.55, "diaryCount": 1 },
    { "date": "2025-07-31", "avgScore": 0.48, "diaryCount": 2 },
    { "date": "2025-08-01", "avgScore": 0.77, "diaryCount": 2 }
  ]
}
```

