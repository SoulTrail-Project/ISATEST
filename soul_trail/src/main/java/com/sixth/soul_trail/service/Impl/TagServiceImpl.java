package com.sixth.soul_trail.service.Impl;

import com.sixth.soul_trail.exception.BusinessException;
import com.sixth.soul_trail.mapper.UserTagMapper;
import com.sixth.soul_trail.pojo.UserTag;
import com.sixth.soul_trail.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户标签库实现，操作 user_tag 表
 *
 * 职责边界：这里只管「用户可以选择哪些标签」，
 *          至于「某篇日记实际打了哪些标签」由 DiaryServiceImpl 管（存在 diary.tags 列）。
 *          用户在标签库新增 / 删除标签，不会影响任何一篇历史日记。
 */
@Service
public class TagServiceImpl implements TagService {

    /** 系统预置标签：来自设计图「标记今天的状态」，新用户首次访问标签库时自动灌入 */
    private static final List<String> PRESET_TAGS = List.of(
            "工作", "学习", "情感", "家庭", "健康",
            "压力", "治愈", "内耗", "阅读", "睡眠");

    private static final int MAX_TAG_LENGTH = 4;   // 单个标签最多字数
    private static final int MAX_TAG_COUNT  = 20;   // 每人最多标签数

    @Autowired
    private UserTagMapper userTagMapper;

    /**
     * 我的标签库
     *
     * 采用「懒初始化」：用户第一次访问时表里没他的数据，
     * 就先把 10 个系统预置标签批量灌进去，之后完全由他自己增删。
     * 好处是不用改注册流程，老用户下次访问也能自动获得预置标签。
     */
    @Override
    public List<String> listTags(Long userId) {
        if (userTagMapper.countByUserId(userId) == 0) {
            userTagMapper.insertBatch(userId, PRESET_TAGS);
        }
        return userTagMapper.selectTagNamesByUserId(userId);
    }

    /**
     * 新增标签
     *
     * 校验顺序：非空 → 字数上限 → 总数量上限 → 是否重复
     * 这里调 listTags(userId) 而不是直接查库，是为了顺带触发懒初始化
     */
    @Override
    public void addTag(Long userId, String tagName) {
        if(tagName == null || tagName.trim().isEmpty()){
            throw new BusinessException(400,"标签名不能为空");
        }
        String name = tagName.trim();

        if (name.length() > MAX_TAG_LENGTH) {
            throw new BusinessException(400, "标签最多 " + MAX_TAG_LENGTH + " 个字");
        }

        List<String> existing = listTags(userId);
        if(existing.size() >= MAX_TAG_COUNT){
            throw new BusinessException(400, "标签最多 " + MAX_TAG_COUNT + " 个");
        }
        if (existing.contains(name)) {
            throw new BusinessException(400, "标签已存在");
        }
        UserTag userTag = new UserTag();
        userTag.setUserId(userId);
        userTag.setTagName(name);
        userTag.setIsPreset(0); // 0 = 用户自建（区别于系统预置的 1）
        userTag.setSortOrder(0);
        userTagMapper.insert(userTag);

    }

    /**
     * 删除标签
     *
     * 只删标签库里的选项，不会去清理历史日记上已打的标签 ——
     * 日记上的 tags 是写入时的快照，这样即使误删标签也不会破坏历史数据。
     */
    @Override
    public void deleteTag(Long userId, String tagName) {
     int rows = userTagMapper.deleteByUserIdAngTagName(userId, tagName);
     if(rows==0) {
         throw new BusinessException(404,"标签不存在");
     }
    }

}
