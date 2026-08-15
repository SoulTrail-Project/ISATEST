package com.sixth.soul_trail.controller;

import com.sixth.soul_trail.VO.EmotionDistributionVO;
import com.sixth.soul_trail.VO.WordCloudVO;
import com.sixth.soul_trail.common.Result;
import com.sixth.soul_trail.service.DiaryService;
import com.sixth.soul_trail.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stats")
public class emotionDistributionController {

    @Autowired
    private DiaryService diaryService;

    @GetMapping("emotion-distribution")
    public Result<List<EmotionDistributionVO>> emotionDistribution() {
        Long userId = SecurityUtil.getCurrentUserId();
        return new Result<>(200,"获取成功",diaryService.emotionDistribution(userId));

    }
    @GetMapping("/word-cloud")
    public Result<List<WordCloudVO>> wordCloud() {
        Long userId = SecurityUtil.getCurrentUserId();
        return new Result<> (200,"获取成功",diaryService.wordCloud(userId));
    }


}
