package com.sixth.soul_trail.mapper;

import com.sixth.soul_trail.VO.WordCloudVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiaryKeywordMapper {
    List<WordCloudVO> selectWordCloud(@Param("userId") Long userId);
}

