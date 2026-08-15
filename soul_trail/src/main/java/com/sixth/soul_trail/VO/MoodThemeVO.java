package com.sixth.soul_trail.VO;

import lombok.Data;

/**
 * 心情主题VO
 * 作用：把心情枚举的信息打包返回给前端
 * 前端拿到直接就能渲染背景、颜色、表情、语录
 */
@Data
public class MoodThemeVO {
    private String code;        // 心情编码，如 "happy"
    private String name;        // 心情中文名，如 "开心"
    private String bgImage;     // 背景图地址
    private String themeColor;  // 主题色（十六进制颜色）
    private String emoji;       // 表情符号
    private String quote;       // 治愈语录
    private String suggestion;  // 小建议
}
