package com.sixth.soul_trail.service;

import com.sixth.soul_trail.VO.DiaryCreateRequestVO;
import com.sixth.soul_trail.VO.DiaryUpdateRequestVO;
import com.sixth.soul_trail.VO.DiaryVO;
import com.sixth.soul_trail.VO.DiaryPageVO;

import java.time.LocalDate;
import java.util.List;

public interface DiaryService {

    DiaryVO create(Long userId, DiaryCreateRequestVO request);

    DiaryPageVO list(Long userId, int page, int pageSize);

    DiaryVO getById(Long userId, Long diaryId);

    DiaryVO update(Long userId, Long diaryId, DiaryUpdateRequestVO request);

    void delete(Long userId, Long diaryId);

    DiaryVO getDiaryByDate(LocalDate localDate,Long userId);
}