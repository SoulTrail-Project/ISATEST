package com.sixth.soul_trail.service;

import java.util.List;

public interface TagService {

    //我的数据库 首次访问自动初始化系统预制标签
    List<String> listTags(Long userId);

    //新增标签
    void addTag(Long userId, String tagName);

    //删除标签
    void deleteTag(Long userId, String tagName);

}
