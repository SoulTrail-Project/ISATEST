package com.sixth.soul_trail.service.Impl;

import com.sixth.soul_trail.VO.DiaryCreateRequestVO;
import com.sixth.soul_trail.VO.DiaryUpdateRequestVO;
import com.sixth.soul_trail.VO.DiaryVO;
import com.sixth.soul_trail.VO.DiaryPageVO;
import com.sixth.soul_trail.VO.EmotionDistributionVO;
import com.sixth.soul_trail.VO.WordCloudVO;
import com.sixth.soul_trail.common.MoodTypeEnum;
import com.sixth.soul_trail.exception.BusinessException;
import com.sixth.soul_trail.mapper.DiaryKeywordMapper;
import com.sixth.soul_trail.mapper.DiaryMapper;
import com.sixth.soul_trail.pojo.Diary;
import com.sixth.soul_trail.service.DiaryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DiaryServiceImpl implements DiaryService {

    @Autowired
    private DiaryMapper diaryMapper;

    @Autowired
    private DiaryKeywordMapper diaryKeywordMapper;

    @Override
    public DiaryVO create(Long userId, DiaryCreateRequestVO request) {
        Diary diary = new Diary();
        diary.setUserId(userId);
        diary.setTitle(request.getTitle() != null ? request.getTitle() : "");
        diary.setContent(request.getContent());
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
            throw new BusinessException(404, "鏃ヨ?颁笉瀛樺??");
        }
        return convertToVO(diary);
    }

    @Override
    public DiaryVO update(Long userId, Long diaryId, DiaryUpdateRequestVO request) {
        Diary diary = diaryMapper.selectByIdAndUserId(diaryId, userId);
        if (diary == null) {
            throw new BusinessException(404, "鏃ヨ?颁笉瀛樺??");
        }
        diary.setTitle(request.getTitle() != null ? request.getTitle() : "");
        diary.setContent(request.getContent());
        diaryMapper.update(diary);

        return convertToVO(diary);
    }

    @Override
    public void delete(Long userId, Long diaryId) {
        int rows = diaryMapper.softDeleteById(diaryId, userId);
        if (rows == 0) {
            throw new BusinessException(404, "鏃ヨ?颁笉瀛樺??");
        }
    }

    /**
     * 瀹炰綋绫昏浆 VO锛岄伩鍏嶆妸 UserId銆乮sDeleted 绛夊唴閮ㄥ瓧娈垫毚闇茬粰鍓嶇??
     */
    private DiaryVO convertToVO(Diary diary) {
        DiaryVO vo = new DiaryVO();
        BeanUtils.copyProperties(diary, vo);
        return vo;
    }

    @Override
    public List<EmotionDistributionVO> emotionDistribution(Long userId) {
        List<Map<String, Object>> rows = diaryMapper.countByEmotionType(userId);
        List<EmotionDistributionVO> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            EmotionDistributionVO vo = new EmotionDistributionVO();
            String moodType = (String) row.get("moodType");
            MoodTypeEnum mood = MoodTypeEnum.getByCode(moodType);
            vo.setMoodType(moodType);
            vo.setName(mood != null ? mood.getName() : moodType);
            vo.setColor(mood != null ? mood.getThemeColor() : "#999999");
            vo.setValue(((Number) row.get("value")).longValue());
            result.add(vo);
        }
        return result;
    }

    @Override
    public List<WordCloudVO> wordCloud(Long userId) {
        return diaryKeywordMapper.selectWordCloud(userId);
    }
}