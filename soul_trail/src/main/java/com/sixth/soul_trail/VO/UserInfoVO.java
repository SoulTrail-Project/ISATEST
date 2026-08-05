package com.sixth.soul_trail.VO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserInfoVO {
    private Long id;            // 若前端要字符串，改成 String 并用 String.valueOf()
    private String username;
    private String nickname;
    private String avatar;      // 对应 User.avatarUrl 头像
    private LocalDateTime createdAt;
}