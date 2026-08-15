package com.sixth.soul_trail.common;

import lombok.Getter;

/**
 * 心情类型枚举
 * 作用：统一管理所有心情的配置（名字、颜色、背景图、语录、建议）
 * 以后加新心情，只需要在这里加一行就行
 */
@Getter
public enum MoodTypeEnum {

    // ========== 10种心情，每种一套完整皮肤 ==========

    HAPPY("happy", "开心",
            "/images/mood/happy-bg.jpg",    // 背景图路径
            "#FFD93D",                       // 主题色（黄色）
            "😊",                            // 表情
            "今天也是被阳光眷顾的一天~",      // 治愈语录
            "把这份开心分享给身边的人吧"),     // 小建议

    EXCITED("excited", "兴奋",
            "/images/mood/excited-bg.jpg",
            "#FF6B6B",
            "🎉",
            "心跳加速的感觉，真好！",
            "记录下让你兴奋的这件事，以后回看会很燃"),

    CALM("calm", "平静",
            "/images/mood/calm-bg.jpg",
            "#74B9FF",
            "🌙",
            "岁月静好，温柔以待",
            "泡杯茶，享受这份宁静"),

    SAD("sad", "难过",
            "/images/mood/sad-bg.jpg",
            "#A0A0A0",
            "🌧️",
            "下雨了，但总会停的",
            "难过的时候抱抱自己，吃点甜的"),

    ANGRY("angry", "生气",
            "/images/mood/angry-bg.jpg",
            "#E17055",
            "🔥",
            "深呼吸，世界如此美妙",
            "先数10个数，再决定要不要发火"),

    ANXIOUS("anxious", "焦虑",
            "/images/mood/anxious-bg.jpg",
            "#A29BFE",
            "🌀",
            "焦虑是因为你太想做好了",
            "把大目标拆成小步骤，一步一步来"),

    TIRED("tired", "疲惫",
            "/images/mood/tired-bg.jpg",
            "#B2BEC3",
            "😴",
            "累了就休息，地球没你照样转",
            "早点睡，明天又是新的一天"),

    PEACEFUL("peaceful", "安宁",
            "/images/mood/peaceful-bg.jpg",
            "#55EFC4",
            "🍃",
            "风很温柔，你也是",
            "出门散散步，感受大自然"),

    LONELY("lonely", "孤独",
            "/images/mood/lonely-bg.jpg",
            "#6C5CE7",
            "⭐",
            "孤独是人生的常态，但你不是一个人",
            "给老朋友发条消息吧"),

    GRATEFUL("grateful", "感恩",
            "/images/mood/grateful-bg.jpg",
            "#FD79A8",
            "💕",
            "拥有的都是幸运",
            "写下三件今天让你感恩的小事");

    // ========== 每个心情带的7个属性 ==========
    private final String code;        // 心情编码（数据库存的就是这个）
    private final String name;        // 心情中文名（显示给用户看）
    private final String bgImage;     // 背景图URL（前端当背景用）
    private final String themeColor;  // 主题色（按钮、文字颜色）
    private final String emoji;       // 表情图标
    private final String quote;       // 治愈语录（每天一句暖心话）
    private final String suggestion;  // 心情建议（给用户的小tips）

    /**
     * 构造方法（枚举必须有）
     */
    MoodTypeEnum(String code, String name, String bgImage, String themeColor,
                 String emoji, String quote, String suggestion) {
        this.code = code;
        this.name = name;
        this.bgImage = bgImage;
        this.themeColor = themeColor;
        this.emoji = emoji;
        this.quote = quote;
        this.suggestion = suggestion;
    }

    /**
     * 根据编码找心情（比如传"happy"返回HAPPY枚举）
     * 找不到默认返回平静心情
     */
    public static MoodTypeEnum getByCode(String code) {
        if (code == null) return CALM;
        for (MoodTypeEnum m : values()) {
            if (m.code.equalsIgnoreCase(code)) return m;
        }
        return CALM;
    }

    /**
     * 根据分数推荐心情（0~1分对应不同心情）
     * 用于没有明确心情类型时，靠分数猜一个
     */
    public static MoodTypeEnum getByScore(double score) {
        if (score >= 0.85) return EXCITED;     // 0.85以上 = 超开心
        if (score >= 0.7) return HAPPY;        // 0.7~0.85 = 开心
        if (score >= 0.55) return PEACEFUL;    // 0.55~0.7 = 安宁
        if (score >= 0.45) return CALM;        // 0.45~0.55 = 平静
        if (score >= 0.3) return ANXIOUS;      // 0.3~0.45 = 焦虑
        if (score >= 0.15) return SAD;         // 0.15~0.3 = 难过
        return TIRED;                           // 0.15以下 = 疲惫
    }
}
