package com.sixth.soul_trail.mapper;

import com.sixth.soul_trail.VO.WordCloudVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 词云相关，操作 diary_keyword 表
 *
 * 注意：diary_keyword 里存的是「算法从日记正文自动抽取的关键词」，
 *     跟「用户手动打的标签」不是一回事 —— 用户标签存在 diary.tags 列（见 DiaryMapper）。
 *     两者不要混用。
 */
@Mapper
public interface DiaryKeywordMapper {

    /** 词云：统计出现次数 >= 3 的关键词，取前 50 */
    List<WordCloudVO> selectWordCloud(@Param("userId") Long userId);

    // ========== 下面三个方法已废弃，暂无任何调用方 ==========
    // 背景：早期曾打算用 diary_keyword 表存用户标签，后改为存 diary.tags（JSON 列）。
    //      保留是为了防止其他地方误以为还能用；确认不再需要后可整块删除（含 XML 里对应语句）。

    /**
     * @deprecated 已废弃：标签改存 diary.tags，不再写 diary_keyword
     */
    @Deprecated
    void insertBatch(@Param("diaryId") Long diaryId, @Param("tags") List<String> tags);

    /**
     * @deprecated 已废弃：标签改存 diary.tags，不再写 diary_keyword
     */
    @Deprecated
    int deleteByDiaryId(@Param("diaryId") Long diaryId);

    /**
     * @deprecated 已废弃：标签改存 diary.tags，不再查 diary_keyword
     */
    @Deprecated
    List<String> selectWordsByDiaryId(@Param("diaryId") Long diaryId);
}

