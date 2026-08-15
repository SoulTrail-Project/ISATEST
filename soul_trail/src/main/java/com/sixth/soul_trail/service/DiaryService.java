package com.sixth.soul_trail.service;

import com.sixth.soul_trail.pojo.Summary;
import com.sixth.soul_trail.VO.EmotionDistributionVO;
import com.sixth.soul_trail.VO.WordCloudVO;
import com.sixth.soul_trail.pojo.Diary;
import java.util.List;

public interface DiaryService {

    Summary create(Long userId, String title, String content);

    List<Summary> list(Long userId, int page, int pageSize);

    long count(Long userId);

    Summary getById(Long userId, Long diaryId);

    Summary update(Long userId, Long diaryId, String title, String content);

    void delete(Long userId, Long diaryId);


    //接口10：情绪统计(饼状图)
    List<EmotionDistributionVO> emotionDistribution(Long userId);
    // 接口11：词云数据
    List<WordCloudVO> wordCloud(Long userId);
}