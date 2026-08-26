package com.sixth.soul_trail.service.Impl;

import com.sixth.soul_trail.VO.DiaryCreateRequestVO;
import com.sixth.soul_trail.VO.DiaryUpdateRequestVO;
import com.sixth.soul_trail.VO.DiaryVO;
import com.sixth.soul_trail.VO.DiaryPageVO;
import com.sixth.soul_trail.exception.BusinessException;
import com.sixth.soul_trail.mapper.DiaryMapper;
import com.sixth.soul_trail.pojo.Diary;
import com.sixth.soul_trail.service.DiaryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DiaryServiceImpl implements DiaryService {

    @Autowired
    private DiaryMapper diaryMapper;

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
        diary.setTitle(request.getTitle() != null ? request.getTitle() : "");
        diary.setContent(request.getContent());
        //新增MoodType
        diary.setMoodType(request.getMoodType());
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
}