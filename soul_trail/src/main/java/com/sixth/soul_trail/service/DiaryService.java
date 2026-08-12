package com.sixth.soul_trail.service;

import com.sixth.soul_trail.pojo.Summary;
import java.util.List;

public interface DiaryService {

    Summary create(Long userId, String title, String content);

    List<Summary> list(Long userId, int page, int pageSize);

    long count(Long userId);

    Summary getById(Long userId, Long diaryId);

    Summary update(Long userId, Long diaryId, String title, String content);

    void delete(Long userId, Long diaryId);
}