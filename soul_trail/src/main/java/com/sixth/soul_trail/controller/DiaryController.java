package com.sixth.soul_trail.controller;

import com.sixth.soul_trail.VO.DiaryCreateRequestVO;
import com.sixth.soul_trail.VO.DiaryUpdateRequestVO;
import com.sixth.soul_trail.VO.DiaryVO;
import com.sixth.soul_trail.VO.DiaryPageVO;
import com.sixth.soul_trail.common.Result;
import com.sixth.soul_trail.mapper.DiaryMapper;
import com.sixth.soul_trail.pojo.Diary;
import com.sixth.soul_trail.service.DiaryService;
import com.sixth.soul_trail.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/diaries")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;
    @Autowired
    private DiaryMapper diaryMapper;

    /**
     * POST /api/diaries
     * 请求体：{ "title": "xxx", "content": "xxx","moodType": "xxx"}
     */
    @PostMapping
    public Result<DiaryVO> create(@RequestBody DiaryCreateRequestVO request) {
        Long userId = SecurityUtil.getCurrentUserId();
        System.out.println("测试Controller能否拿到userId:" + userId);
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            return Result.error(400, "日记内容不能为空");
        }

        DiaryVO diary = diaryService.create(userId, request);
        return Result.success(diary);
    }

    /**
     * GET /api/diaries?page=1&pageSize=10
     */
    @GetMapping
    public Result<DiaryPageVO> list(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int pageSize) {
        Long userId = SecurityUtil.getCurrentUserId();
        DiaryPageVO pageVO = diaryService.list(userId, page, pageSize);
        return Result.success(pageVO);
    }

    /**
     * GET /api/diaries/{id}
     */
    @GetMapping("/{id}")
    public Result<DiaryVO> getById(@PathVariable("id") Long diaryId) {
        Long userId = SecurityUtil.getCurrentUserId();
        DiaryVO diary = diaryService.getById(userId, diaryId);
        return Result.success(diary);
    }

    /**
     * PUT /api/diaries/{id}
     * 请求体：{ "title": "xxx", "content": "xxx" }
     */
    @PutMapping("/{id}")
    public Result<DiaryVO> update(@PathVariable("id") Long diaryId,
                                  @RequestBody DiaryUpdateRequestVO request) {
        Long userId = SecurityUtil.getCurrentUserId();

        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            return Result.error(400, "日记内容不能为空");
        }

        DiaryVO diary = diaryService.update(userId, diaryId, request);
        return Result.success(diary);
    }

    /**
     * DELETE /api/diaries/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long diaryId) {
        Long userId = SecurityUtil.getCurrentUserId();
        diaryService.delete(userId, diaryId);
        return Result.success(null);
    }

    @GetMapping
    public Result<DiaryVO> getDiaryByDate(@RequestParam(defaultValue = "2026-01-01") String date) {
        Long userId = SecurityUtil.getCurrentUserId();
        LocalDate localDate = LocalDate.parse(date);
        DiaryVO diaryVO = diaryService.getDiaryByDate(localDate, userId);
        return Result.success(diaryVO);
    }

}