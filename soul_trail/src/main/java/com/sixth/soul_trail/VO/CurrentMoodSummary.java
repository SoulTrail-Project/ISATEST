package com.sixth.soul_trail.VO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrentMoodSummary {
    private String mostFrequentMood;
    private int mostFrequentMoodCount;
}
