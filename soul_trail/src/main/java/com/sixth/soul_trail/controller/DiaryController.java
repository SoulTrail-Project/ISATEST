package com.sixth.soul_trail.controller;

import com.sixth.soul_trail.VO.DiaryCreateRequestVO;
import com.sixth.soul_trail.VO.DiaryUpdateRequestVO;
import com.sixth.soul_trail.VO.DiaryVO;
import com.sixth.soul_trail.VO.DiaryPageVO;
import com.sixth.soul_trail.common.Result;
import com.sixth.soul_trail.service.DiaryService;
import com.sixth.soul_trail.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/diaries")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

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

    /**
     * GET /api/diaries?date=2026-08-27
     * 按日期查当天日记。没有则 data 为 null
     */
    @GetMapping(params = "date")
    public Result<List<DiaryVO>> getDiaryByDate(@RequestParam(defaultValue = "2026-01-01") String date) {
        Long userId = SecurityUtil.getCurrentUserId();
        LocalDate localDate = LocalDate.parse(date);
        List<DiaryVO> diaryVOList = diaryService.getDiaryByDate(localDate, userId);
        return Result.success(diaryVOList);
    }

    /**
     * GET /api/diaries/tags/top?limit=5
     * 本周高频标签：统计本周一至今，哪些标签在日记里出现得最多
     * 用于前端「本周小回顾 → 高频标签」，设计图里展示 2 个，所以前端一般传 limit=2
     * 注意：这个接口读的是 diary.tags（日记实际打的标签），
     *      跟 GET /api/tags（用户标签库，存在 user_tag 表）是两套数据，别混用
     */
    @GetMapping("/tags/top")
    public Result<List<String>> getTopTags(@RequestParam(defaultValue = "5") int limit) {
        Long userId = SecurityUtil.getCurrentUserId();
        return Result.success(diaryService.getTopTags(userId, limit));
    }

    @GetMapping(params = "keywords")
    public Result<DiaryPageVO> getDiaryByKeyword(@RequestParam(defaultValue = "") String keyword,
                                                 @RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "10") int pageSize) {
        if (keyword.isEmpty()) {
            return Result.error(401,"关键词不能为空");
        }
        Long userId = SecurityUtil.getCurrentUserId();
        DiaryPageVO diaryPageVO = diaryService.getDiaryByKeyword(userId, keyword, page, pageSize);
        return Result.success(diaryPageVO);
    }

    @GetMapping("/{id}/prev-next")
    public Result<List<DiaryVO>> getPrevNextDiary(@PathVariable("id") Long diaryId) {
        Long userId = SecurityUtil.getCurrentUserId();
        List<DiaryVO> diaryVOList = diaryService.getPrevNextDiary(userId, diaryId);
        return Result.success(diaryVOList);
    }

}