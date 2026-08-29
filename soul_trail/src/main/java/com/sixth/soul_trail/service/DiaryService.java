package com.sixth.soul_trail.service;

import com.sixth.soul_trail.VO.DiaryCreateRequestVO;
import com.sixth.soul_trail.VO.DiaryUpdateRequestVO;
import com.sixth.soul_trail.VO.DiaryVO;
import com.sixth.soul_trail.VO.DiaryPageVO;

import java.time.LocalDate;
import java.util.List;

public interface DiaryService {

    /**
     * 写日记。request.tags 为该篇日记要打的标签，随日记一起存入 diary.tags 列
     */
    DiaryVO create(Long userId, DiaryCreateRequestVO request);

    DiaryPageVO list(Long userId, int page, int pageSize);

    DiaryVO getById(Long userId, Long diaryId);

    /**
     * 改日记。若传了 tags 则全量覆盖（前端传最终数组，不传则保持原样）
     */
    DiaryVO update(Long userId, Long diaryId, DiaryUpdateRequestVO request);

    void delete(Long userId, Long diaryId);

    DiaryVO getDiaryByDate(LocalDate localDate, Long userId);

    /**
     * 本周高频标签：按本周一至今，从 diary.tags 统计使用次数倒排
     *
     * @param limit 返回条数，前端「本周小回顾」一般取 2 条
     */
    List<String> getTopTags(Long userId, int limit);
}