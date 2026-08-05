package com.sixth.soul_trail.VO;

import lombok.Data;

@Data
public class LoginVO {
    private String token;
    private Long expiresIn;     // 86400 秒
    private UserInfoVO userInfo;
}