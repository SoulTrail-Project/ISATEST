package com.sixth.config;

@Configuration
public class SecurityConfig {

    // 注册时，给表单中密码进行BCrypt加密
    @Bean
    public PasswordEncoder passwordEncoder() return new BCryptPaaawordEncoder();

}
