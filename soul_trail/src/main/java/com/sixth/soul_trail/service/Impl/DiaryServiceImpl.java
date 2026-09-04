package com.sixth.soul_trail.service.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sixth.soul_trail.VO.*;
import com.sixth.soul_trail.exception.BusinessException;
import com.sixth.soul_trail.mapper.DiaryMapper;
import com.sixth.soul_trail.pojo.Diary;
import com.sixth.soul_trail.service.DiaryService;
import com.sixth.soul_trail.utils.SecurityUtil;
import com.sixth.soul_trail.utils.SentimentClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 日记业务实现
 *
 * 关于标签的两套数据（务必分清）：
 *   1. 用户标签库  —— 存在 user_tag 表，由 TagService 管，含义是「我可以选择哪些标签」
 *   2. 日记的标签  —— 存在 diary.tags 列（JSON 数组），由本类管，含义是「这篇日记实际打了哪些标签」
 * 两者互相独立：在标签库加了标签不会自动打给任何日记，必须写日记时勾选才会写入 diary.tags。
 */
@Service
@Slf4j
public class DiaryServiceImpl implements DiaryService {

    /** 一篇日记最多能打多少个标签（跟标签库的「每人最多 20 个」是两个独立上限） */
    private static final int MAX_TAGS_PER_DIARY = 10;

    @Autowired
    private DiaryMapper diaryMapper;

    @Autowired
    private SentimentClient sentimentClient;

    /**
     * Jackson 的 JSON 工具，由 Spring Boot 自动装配，用于 List<String> 和 JSON 字符串互转
     */
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public DiaryVO create(Long userId, DiaryCreateRequestVO request) {
        Diary diary = new Diary();
        diary.setUserId(userId);
        diary.setTitle(request.getTitle() != null ? request.getTitle() : "");
        diary.setContent(request.getContent());
        //新添加moodType
        diary.setMoodType(request.getMoodType());
        //补充 diaryDate，避免数据库 NOT NULL 约束报错
        diary.setDiaryDate(LocalDate.now());
        // 标签转成 JSON 字符串，跟日记同一条 INSERT 写入 diary.tags 列
        diary.setTags(toJsonArray(request.getTags()));

        // 调情感分析算法服务（不在线/超时返回 null，日记照存，分数留空）
        Map<String, Object> sentiment = sentimentClient.analyze(request.getContent());
        if (sentiment != null) {
            diary.setSentimentScore((Double) sentiment.get("score"));
            diary.setSentimentLabel((String) sentiment.get("label"));   // 注意实体字段拼写为 sentimentLabel
            diary.setSentimentEmotion((String) sentiment.get("label"));
        }

        diaryMapper.insert(diary);

        return convertToVO(diary);
    }

    @Override
    public DiaryPageVO list(Long userId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Diary> diaryList = diaryMapper.selectPageByUserId(userId, offset, pageSize);
        long total = diaryMapper.countByUserId(userId);

        List<DiaryVO> records = new ArrayList<>();
        for (Diary diary : diaryList) {
            records.add(convertToVO(diary));
        }

        DiaryPageVO pageVO = new DiaryPageVO();
        pageVO.setRecords(records);
        pageVO.setTotal(total);
        pageVO.setPage(page);
        pageVO.setPageSize(pageSize);
        return pageVO;
    }

    @Override
    public DiaryVO getById(Long userId, Long diaryId) {
        Diary diary = diaryMapper.selectByIdAndUserId(diaryId, userId);
        if (diary == null) {
            throw new BusinessException(404, "日记不存在");
        }
        return convertToVO(diary);
    }

    @Override
    public DiaryVO update(Long userId, Long diaryId, DiaryUpdateRequestVO request) {
        Diary diary = diaryMapper.selectByIdAndUserId(diaryId, userId);
        if (diary == null) {
            throw new BusinessException(404, "日记不存在");
        }
        //记得判断正文非空
        if(request.getContent() !=null && !request.getContent().trim().isEmpty()){
            diary.setContent(request.getContent());
            Map<String, Object> sentiment = sentimentClient.analyze(request.getContent());
            if (sentiment != null) {
                diary.setSentimentScore((Double) sentiment.get("score"));
                diary.setSentimentLabel((String) sentiment.get("label"));
                diary.setSentimentEmotion((String) sentiment.get("label"));
            }
        }
        if (request.getTitle() != null) {
            diary.setTitle(request.getTitle());
        }
        if (request.getMoodType() != null) {
            diary.setMoodType(request.getMoodType());
        }

        // 标签全量替换：前端传最终数组，传了就整体覆盖；不传（null）则保持原样不动。
        // 想删掉某个标签，前端传「去掉该标签后的新数组」即可，不需要单独的删除接口。
        if (request.getTags() != null) {
            diary.setTags(toJsonArray(request.getTags()));
        }

        diaryMapper.update(diary);

        return convertToVO(diary);
    }

    @Override
    public void delete(Long userId, Long diaryId) {
        int rows = diaryMapper.softDeleteById(diaryId, userId);
        if (rows == 0) {
            throw new BusinessException(404, "日记不存在");
        }
    }

    @Override
    public List<DiaryVO> getDiaryByDate(LocalDate localDate, Long userId) {
        List<Diary> diaryList = diaryMapper.selectDiaryDate(localDate, userId);
        if (diaryList.isEmpty()) {
            return null;   // 前端拿到 data = null，符合文档约定
        }
        List<DiaryVO> diaryVOList = new ArrayList<>();
        for (Diary diary : diaryList) {
            DiaryVO diaryVO = new DiaryVO();
            BeanUtils.copyProperties(diary, diaryVO);
            if (diary.getSentimentScore() != null) {
                diaryVO.setScore(diary.getSentimentScore().floatValue());
            }
            diaryVO.setTags(fromJsonArray(diary.getTags()));
            diaryVOList.add(diaryVO);
        }
        return diaryVOList;
    }

    @Override
    public List<String> getTopTags(Long userId) {
        // 统计区间：本周一 ~ 今天，对应设计图「本周小回顾」
        // 固定返回 Top2，条数上限写死在 SQL 的 LIMIT 2，与前端约定不开放 limit 参数
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY);
        return diaryMapper.selectTopTagsByUserId(userId, monday, today);
    }

    // ==================== 私有工具方法 ====================

    /**
     * 清洗标签：去 null、去前后空白、去空串、去重，最多保留 MAX_TAGS_PER_DIARY 个
     */
    private List<String> normalizeTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return Collections.emptyList();
        }
        return tags.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .limit(MAX_TAGS_PER_DIARY)
                .collect(Collectors.toList());
    }

    /**
     * List<String> 转 JSON 字符串，写入 diary.tags 列
     *
     * @return 清洗后的 JSON 数组字符串；若没有有效标签则返回 null
     *         注意：MySQL 的 JSON 列不接受空字符串，所以这里必须返回 null 而不是 ""
     */
    private String toJsonArray(List<String> tags) {
        List<String> cleaned = normalizeTags(tags);
        if (cleaned.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(cleaned);
        } catch (Exception e) {
            // 序列化失败不该让日记存不进去，退化为不打标签
            return null;
        }
    }

    /**
     * JSON 字符串转 List<String>，用于把 diary.tags 读出来返回给前端
     *
     * @return 标签列表；null / 空串 / 解析失败都返回空集合，避免接口 500
     */
    private List<String> fromJsonArray(String json) {
        if (json == null || json.isBlank()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * 实体类转 VO，避免把 UserId、isDeleted 等内部字段暴露给前端
     * 标签直接从已查出的 diary 对象里解析 JSON，不需要额外查库（避免了 N+1 查询）
     */
    private DiaryVO convertToVO(Diary diary) {
        DiaryVO vo = new DiaryVO();
        if (diary.getSentimentScore() != null) {
            vo.setScore(diary.getSentimentScore().floatValue());
        }
        BeanUtils.copyProperties(diary, vo);
        vo.setTags(fromJsonArray(diary.getTags()));
        return vo;
    }

    @Override
    public DiaryPageVO getDiaryByKeyword(Long userId, String keyword, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Diary> diaryList = diaryMapper.selectKeyword(userId,keyword,offset,pageSize);
        long total = diaryMapper.countByUserId(userId);
        List<DiaryVO> diaryVOList = new ArrayList<>();
        for (Diary diary : diaryList) {
            diaryVOList.add(convertToVO(diary));
        }
        DiaryPageVO diaryPageVO = new DiaryPageVO();
        diaryPageVO.setRecords(diaryVOList);
        diaryPageVO.setTotal(total);
        diaryPageVO.setPage(page);
        diaryPageVO.setPageSize(pageSize);
        return diaryPageVO;
    };

    @Override
    public List<DiaryVO> getPrevNextDiary(Long userId, Long diaryId) {
        Long prevId = diaryId - 1;
        Long nextId = diaryId + 1;
        Diary prevDiary = diaryMapper.selectByIdAndUserId(prevId, userId);
        Diary nextDiary = diaryMapper.selectByIdAndUserId(nextId, userId);
        List<DiaryVO> diaryVOList = new ArrayList<>();
        // 备注：前一天的日记放在索引0，后一天的日记放在索引1
        diaryVOList.set(0,convertToVO(prevDiary));
        diaryVOList.set(1,convertToVO(nextDiary));
        return diaryVOList;
    }

    @Override
    public List<DiaryExportVO> getExportData() {
        Long userId = SecurityUtil.getCurrentUserId();
        List<Diary> diaryList = diaryMapper.selectExportData(userId);
        return diaryList.stream()
                .map(this::convertToExportVO)
                .collect(Collectors.toList());
    }

    public DiaryExportVO convertToExportVO(Diary diary) {
        DiaryExportVO diaryExportVO = new DiaryExportVO();
        diaryExportVO.setDiaryId(diary.getId());
        String content = diary.getContent();
        if (!content.isEmpty()) {
            diaryExportVO.setContent(content.substring(0, 20) + "...");
        } else {
            diaryExportVO.setContent(content);
        }
        diaryExportVO.setMoodType(diary.getMoodType());
        if (diary.getCreatedAt() != null) {
            // 使用 Java 8 标准日期格式化器
            java.time.format.DateTimeFormatter formatter =
                    java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            diaryExportVO.setCreatedAt(diary.getCreatedAt().format(formatter));
        }
        return diaryExportVO;
    }

}
