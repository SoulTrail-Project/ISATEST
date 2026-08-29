package com.sixth.soul_trail.controller;

import com.sixth.soul_trail.common.Result;
import com.sixth.soul_trail.service.TagService;
import com.sixth.soul_trail.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户标签库接口
 *
 * 说明：这里管的是「我可以选择哪些标签」（存在 user_tag 表），
 *      跟「某篇日记实际打了哪些标签」（存在 diary.tags 列）是两套独立数据。
 *      用户在标签库里加了标签，不会自动打在任何日记上；必须写日记时勾选才会写入 diary.tags。
 *
 * 注意：userId 一律从 token 解析（SecurityUtil），绝不由前端传参，防止越权删改他人标签。
 */
@RestController
@RequestMapping("/api/tags")
public class TagController {
    @Autowired
    private TagService tagService;

    /**
     * GET /api/tags
     * 我的标签库。首次访问时若该用户没有任何标签，会自动灌入 10 个系统预置标签（懒初始化）
     */
    @GetMapping
    public Result<List<String>> list() {
        Long userId = SecurityUtil.getCurrentUserId();
        return Result.success(tagService.listTags(userId));
    }

    /**
     * POST /api/tags
     * 新增标签，请求体：{"tagName":"运动"}
     * 校验：不能为空、最多 4 字、去重、每人最多 20 个（见 TagServiceImpl）
     */
    @PostMapping
    public Result<String> addTag(@RequestBody Map<String, String> body) {
        Long userId = SecurityUtil.getCurrentUserId();
        tagService.addTag(userId, body.get("tagName"));
        return Result.success(null);
    }

    /**
     * DELETE /api/tags?tagName=运动
     * 删除标签。只删标签库里的选项，不影响历史日记上已打的标签（历史数据是快照）
     */
    @DeleteMapping
    public Result<String> deleteTag(@RequestParam String tagName) {
        Long userId = SecurityUtil.getCurrentUserId();
        tagService.deleteTag(userId, tagName);
        return Result.success(null);
    }
}
