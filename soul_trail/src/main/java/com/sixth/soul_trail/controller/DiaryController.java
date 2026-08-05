package com.sixth.soul_trail.controller;

import com.sixth.soul_trail.common.Result;
import com.sixth.soul_trail.pojo.Diary;
import com.sixth.soul_trail.service.DiaryService;
import com.sixth.soul_trail.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/diaries")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    /**
     * POST /api/diaries
     * 请求体：{ "title": "xxx", "content": "xxx" }
     */
    @PostMapping
    public Result<Diary> create(@RequestBody Map<String, String> body) {
        Long userId = SecurityUtil.getCurrentUserId();
        String title = body.get("title");
        String content = body.get("content");

        if (content == null || content.trim().isEmpty()) {
            return Result.error(400, "日记内容不能为空");
        }

        Diary diary = diaryService.create(userId, title, content);
        return Result.success(diary);
    }

    /**
     * GET /api/diaries?page=1&pageSize=10
     */
    @GetMapping
    public Result<Map<String, Object>> list(@RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int pageSize) {
        Long userId = SecurityUtil.getCurrentUserId();
        List<Diary> records = diaryService.list(userId, page, pageSize);
        long total = diaryService.count(userId);

        Map<String, Object> data = new HashMap<>();
        data.put("records", records);
        data.put("total", total);
        data.put("page", page);
        data.put("pageSize", pageSize);

        return Result.success(data);
    }

    /**
     * GET /api/diaries/{id}
     */
    @GetMapping("/{id}")
    public Result<Diary> getById(@PathVariable("id") Long diaryId) {
        Long userId = SecurityUtil.getCurrentUserId();
        Diary diary = diaryService.getById(userId, diaryId);
        return Result.success(diary);
    }

    /**
     * PUT /api/diaries/{id}
     * 请求体：{ "title": "xxx", "content": "xxx" }
     */
    @PutMapping("/{id}")
    public Result<Diary> update(@PathVariable("id") Long diaryId,
                                @RequestBody Map<String, String> body) {
        Long userId = SecurityUtil.getCurrentUserId();
        String title = body.get("title");
        String content = body.get("content");

        if (content == null || content.trim().isEmpty()) {
            return Result.error(400, "日记内容不能为空");
        }

        Diary diary = diaryService.update(userId, diaryId, title, content);
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
}