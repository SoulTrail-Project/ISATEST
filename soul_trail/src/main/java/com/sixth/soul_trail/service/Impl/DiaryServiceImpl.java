package com.sixth.soul_trail.service.impl;

import com.sixth.soul_trail.VO.DiaryCreateRequestVO;
import com.sixth.soul_trail.VO.DiaryUpdateRequestVO;
import com.sixth.soul_trail.VO.DiaryVO;
import com.sixth.soul_trail.VO.DiaryPageVO;
import com.sixth.soul_trail.VO.EmotionDistributionVO;
import com.sixth.soul_trail.VO.WordCloudVO;
import com.sixth.soul_trail.exception.BusinessException;
import com.sixth.soul_trail.mapper.DiaryKeywordMapper;
import com.sixth.soul_trail.mapper.DiaryMapper;
import com.sixth.soul_trail.pojo.Summary;
import com.sixth.soul_trail.service.DiaryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DiaryServiceImpl implements DiaryService {

    @Autowired
    private DiaryMapper diaryMapper;

    @Autowired
    private DiaryKeywordMapper diaryKeywordMapper;

    // moodType -> {中文名, 颜色}，用于接口10情绪分布映射
    private static final Map<String, String[]> MOOD_META = new HashMap<>();
    static {
        MOOD_META.put("happy",   new String[]{"开心", "#FFD700"});
        MOOD_META.put("calm",    new String[]{"平静", "#87CEEB"});
        MOOD_META.put("anxious", new String[]{"焦虑", "#FFA500"});
        MOOD_META.put("sad",     new String[]{"难过", "#6495ED"});
        MOOD_META.put("angry",   new String[]{"愤怒", "#FF4500"});
        MOOD_META.put("tired",   new String[]{"疲惫", "#808080"});
    }

    @Override
    public DiaryVO create(Long userId, DiaryCreateRequestVO request) {
        Diary diary = new Diary();
    public Summary create(Long userId, String title, String content) {
        Summary diary = new Summary();
        diary.setUserId(userId);
        diary.setTitle(request.getTitle() != null ? request.getTitle() : "");
        diary.setContent(request.getContent());
        diaryMapper.insert(diary);

        return convertToVO(diary);
    }

    @Override
    public DiaryPageVO list(Long userId, int page, int pageSize) {
    public List<Summary> list(Long userId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Diary> diaryList = diaryMapper.selectPageByUserId(userId, offset, pageSize);
        long total = diaryMapper.countByUserId(userId);

        List<DiaryVO> records = new ArrayList<>();
        for (Diary diary : diaryList) {
            records.add(convertToVO(diary));
        }

        DiaryPageVO pageVO = new DiaryPageVO();
        pageVO.setRecords(records);
        pageVO.setTotal(total);
        pageVO.setPage(page);
        pageVO.setPageSize(pageSize);
        return pageVO;
    }

    @Override
    public DiaryVO getById(Long userId, Long diaryId) {
        Diary diary = diaryMapper.selectByIdAndUserId(diaryId, userId);
    public Summary getById(Long userId, Long diaryId) {
        Summary diary = diaryMapper.selectByIdAndUserId(diaryId, userId);
        if (diary == null) {
            throw new BusinessException(404, "日记不存在");
        }
        return convertToVO(diary);
    }

    @Override
    public DiaryVO update(Long userId, Long diaryId, DiaryUpdateRequestVO request) {
        Diary diary = diaryMapper.selectByIdAndUserId(diaryId, userId);
    public Summary update(Long userId, Long diaryId, String title, String content) {
        Summary diary = diaryMapper.selectByIdAndUserId(diaryId, userId);
        if (diary == null) {
            throw new BusinessException(404, "日记不存在");
        }
        diary.setTitle(request.getTitle() != null ? request.getTitle() : "");
        diary.setContent(request.getContent());
        diaryMapper.update(diary);

        return convertToVO(diary);
    }

    @Override
    public void delete(Long userId, Long diaryId) {
        int rows = diaryMapper.softDeleteById(diaryId, userId);
        if (rows == 0) {
            throw new BusinessException(404, "日记不存在");
        }
    }

    /**
     * 实体类转 VO，避免把 UserId、isDeleted 等内部字段暴露给前端
     */
    private DiaryVO convertToVO(Diary diary) {
        DiaryVO vo = new DiaryVO();
        BeanUtils.copyProperties(diary, vo);
        return vo;
    }
    @Override
    public List<EmotionDistributionVO> emotionDistribution(Long userId) {
        List<Map<String,Object>> rows = diaryMapper.countByEmotionType(userId);
        List<EmotionDistributionVO> result = new ArrayList<>();
        for (Map<String,Object> row : rows) {
            String mood = (String) row.get("moodType");
            Long cnt = ((Number) row.get("value")).longValue();
            String[] meta = MOOD_META.getOrDefault(mood, new String[]{mood, "#CCCCCC"});
            EmotionDistributionVO vo = new EmotionDistributionVO();
            vo.setName(meta[0]); vo.setValue(cnt); vo.setMoodType(mood); vo.setColor(meta[1]);
            result.add(vo);
        }
        return result;
    }

    @Override
    public List<WordCloudVO> wordCloud(Long userId) {
        return diaryKeywordMapper.selectWordCloud(userId);
    }

}