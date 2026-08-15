package com.sixth.soul_trail.pojo;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class User {
    private Long id;            // 主键（API 中返回的 userId）
    private String username;    // 登录名，唯一
    private String password;    // bcrypt 哈希，绝不返回前端
    private String nickname;    // 昵称，缺省=username
    private String avatarUrl;   // 头像，可空
    private Integer role;       // 0=普通用户 1=管理员（管理后台用）
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String test1;
}