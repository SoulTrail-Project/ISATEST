package com.sixth.soul_trail.VO;

import lombok.Data;

@Data
public class UpdatePasswordRequestVO {
    private String oldPassword;      // 原密码
    private String newPassword;      // 新密码
    private String confirmPassword;  // 确认新密码
}
