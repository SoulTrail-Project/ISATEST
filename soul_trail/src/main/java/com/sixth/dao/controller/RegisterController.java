package com.sixth.dao.controller;


@RestController
@RequestMapping("/user")
public class RegisterController {

    @Autowired
    private UserService userService;

    /*
    * 用户注册接口
    * */
    @POSTMapping("/register")
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
