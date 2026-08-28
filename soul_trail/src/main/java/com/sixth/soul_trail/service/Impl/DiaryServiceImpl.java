package com.sixth.soul_trail.service.Impl;

import com.sixth.soul_trail.VO.DiaryCreateRequestVO;
import com.sixth.soul_trail.VO.DiaryUpdateRequestVO;
import com.sixth.soul_trail.VO.DiaryVO;
import com.sixth.soul_trail.VO.DiaryPageVO;
import com.sixth.soul_trail.exception.BusinessException;
import com.sixth.soul_trail.mapper.DiaryMapper;
import com.sixth.soul_trail.pojo.Diary;
import com.sixth.soul_trail.service.DiaryService;
import com.sixth.soul_trail.utils.SentimentClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DiaryServiceImpl implements DiaryService {

    @Autowired
    private DiaryMapper diaryMapper;

    @Autowired
    private SentimentClient sentimentClient;

    @Override
    public DiaryVO create(Long userId, DiaryCreateRequestVO request) {
        Diary diary = new Diary();
        diary.setUserId(userId);
        diary.setTitle(request.getTitle() != null ? request.getTitle() : "");
        diary.setContent(request.getContent());
        //新添加moodType
        diary.setMoodType(request.getMoodType());
        //补充 diaryDate，避免数据库 NOT NULL 约束报错
        diary.setDiaryDate(LocalDate.now());

        // 调情感分析算法服务（不在线/超时返回 null，日记照存，分数留空）
        Map<String, Object> sentiment = sentimentClient.analyze(request.getContent());
        if (sentiment != null) {
            diary.setSentimentScore((Double) sentiment.get("score"));
            diary.setSentimentLabel((String) sentiment.get("label"));   // 注意实体字段拼写为 sentimentLabel
            diary.setSentimentEmotion((String) sentiment.get("label"));
        }

        diaryMapper.insert(diary);

        return convertToVO(diary);
    }

    @Override
    public DiaryPageVO list(Long userId, int page, int pageSize) {
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
        if (diary == null) {
            throw new BusinessException(404, "日记不存在");
        }
        return convertToVO(diary);
    }

    @Override
    public DiaryVO update(Long userId, Long diaryId, DiaryUpdateRequestVO request) {
        Diary diary = diaryMapper.selectByIdAndUserId(diaryId, userId);
        if (diary == null) {
            throw new BusinessException(404, "日记不存在");
        }
        //记得判断正文非空
        if(request.getContent() !=null && !request.getContent().trim().isEmpty()){
            diary.setContent(request.getContent());
            Map<String, Object> sentiment = sentimentClient.analyze(request.getContent());
            if (sentiment != null) {
                diary.setSentimentScore((Double) sentiment.get("score"));
                diary.setSentimentLabel((String) sentiment.get("label"));
                diary.setSentimentEmotion((String) sentiment.get("label"));
            }
        }
        if (request.getTitle() != null) {
            diary.setTitle(request.getTitle());
        }
        if (request.getMoodType() != null) {
            diary.setMoodType(request.getMoodType());
        }
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
        if (diary.getSentimentScore() != null) {
            vo.setScore(diary.getSentimentScore().floatValue());
        }
        BeanUtils.copyProperties(diary, vo);
        return vo;
    }

    @Override
    public DiaryVO getDiaryByDate(LocalDate localDate, Long userId) {
        Diary diary = diaryMapper.selectDiaryDate(localDate,userId);
        DiaryVO diaryVO = new DiaryVO();
        BeanUtils.copyProperties(diary, diaryVO);
        return diaryVO;
    }

}