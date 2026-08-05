package com.sixth.soul_trail.controller;

import com.sixth.soul_trail.VO.LoginVO;
import com.sixth.soul_trail.VO.UserInfoVO;
import com.sixth.soul_trail.pojo.User;
import com.sixth.soul_trail.service.UserService;
import com.sixth.soul_trail.common.Result;
import com.sixth.soul_trail.utils.JwtUtil;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
        if (user.getUsername() == null || user.getPassword() == null) return Result.error(400,"用户、密码不能为空");
        // 交给service层进行注册业务，并返回结果
        boolean success = userService.register(user);
        // 根据结果进行处理
        // 前端记得重定向到登录界面
        return success ? Result.success("注册成功，请登录") : Result.error(400,"用户名已存在");
    }
    /*
    * 用户登录接口
     */
    @PostMapping("/login")
    public Result login(@RequestBody User loginUser) {
        //判断用户是否存在
        User dbUser = userService.findByUserName(loginUser.getUsername());
        if  (dbUser == null) {
            return Result.error(400,"用户不存在");
        }
        //判断密码是否正确
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(!passwordEncoder.matches(loginUser.getPassword(), dbUser.getPassword())) {
            return Result.error(400,"密码错误");
        }
        String token = JwtUtil.genToken(loginUser.getId());
        //用户信息
        UserInfoVO info = new UserInfoVO();
        info.setId(dbUser.getId());
        info.setUsername(dbUser.getUsername());
        info.setNickname(dbUser.getNickname());
        info.setAvatar(dbUser.getAvatarUrl());          // avatarUrl → avatar
        info.setCreatedAt(dbUser.getCreatedAt());
        //登录信息
        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setExpiresIn(JwtUtil.EXPIRE_SECONDS);
        vo.setUserInfo(info);//
        return new Result<>(200,"登录成功",vo);
    }

}
