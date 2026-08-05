package com.sixth.soul_trail.service.Impl;

import com.sixth.soul_trail.mapper.UserMapper;
import com.sixth.soul_trail.pojo.User;
import com.sixth.soul_trail.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    /*
    * @param user 用户表单
    * @return true/false
     */

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean register(User user) {
        // 1.查询用户名是否存在
        // 1.1简言之就是我拿注册表单上的名字去数据库里查，如果没结果就说明这个名字没被用过
        User existUser = userMapper.selectByUsername(user.getUsername());
        if (existUser != null) return false;

        // 2.前面检查没有问题，准备插入数据库
        // 2.1给密码进行BCrypt加密，登录时必须使用matches()比对
        String bcryptPwd = passwordEncoder.encode(user.getPassword());
        user.setPassword(bcryptPwd);

        // 2.2插入数据库
        int rows = userMapper.insert(user);
        return rows > 0;

    }

}
