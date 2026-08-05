package com.sixth.soul_trail.controller;

import com.sixth.soul_trail.pojo.User;
import com.sixth.soul_trail.service.UserService;
import com.sixth.soul_trail.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /*
    * 用户注册接口
    * */
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        // 校验用户表单是否为空，为空打回
        if (user.getUsername() == null || user.getPassword() == null) return Result.error("用户、密码不能为空");
        // 交给service层进行注册业务，并返回结果
        boolean success = userService.register(user);
        // 根据结果进行处理
        // 前端记得重定向到登录界面
        return success ? Result.success("注册成功，请登录") : Result.error("用户名已存在");
    }

}
