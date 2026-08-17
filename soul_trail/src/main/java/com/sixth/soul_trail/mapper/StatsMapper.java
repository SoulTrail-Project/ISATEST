package com.sixth.soul_trail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sixth.soul_trail.pojo.Diary;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StatsMapper extends BaseMapper<Diary> {
}
