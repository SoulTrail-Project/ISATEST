package com.sixth.soul_trail.service.Impl;

import com.sixth.soul_trail.exception.BusinessException;
import com.sixth.soul_trail.mapper.DiaryMapper;
import com.sixth.soul_trail.pojo.Summary;
import com.sixth.soul_trail.service.DiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiaryServiceImpl implements DiaryService {

    @Autowired
    private DiaryMapper diaryMapper;

    @Override
    public Summary create(Long userId, String title, String content) {
        Summary diary = new Summary();
        diary.setUserId(userId);
        diary.setTitle(title != null ? title : "");
        diary.setContent(content);
        diaryMapper.insert(diary);
        return diary;
    }

    @Override
    public List<Summary> list(Long userId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return diaryMapper.selectPageByUserId(userId, offset, pageSize);
    }

    @Override
    public long count(Long userId) {
        return diaryMapper.countByUserId(userId);
    }

    @Override
    public Summary getById(Long userId, Long diaryId) {
        Summary diary = diaryMapper.selectByIdAndUserId(diaryId, userId);
        if (diary == null) {
            throw new BusinessException(404, "日记不存在");
        }
        return diary;
    }

    @Override
    public Summary update(Long userId, Long diaryId, String title, String content) {
        Summary diary = diaryMapper.selectByIdAndUserId(diaryId, userId);
        if (diary == null) {
            throw new BusinessException(404, "日记不存在");
        }
        diary.setTitle(title != null ? title : "");
        diary.setContent(content);
        diaryMapper.update(diary);
        return diary;
    }

    @Override
    public void delete(Long userId, Long diaryId) {
        int rows = diaryMapper.softDeleteById(diaryId, userId);
        if (rows == 0) {
            throw new BusinessException(404, "日记不存在");
        }
    }
}