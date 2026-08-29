package com.sixth.soul_trail.service.Impl;

import com.sixth.soul_trail.VO.UserInfoVO;
import com.sixth.soul_trail.exception.BusinessException;
import com.sixth.soul_trail.mapper.UserMapper;
import com.sixth.soul_trail.pojo.User;
import com.sixth.soul_trail.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
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

    @Override
    public User findByUserName(String username) {
        //1.查询用户名是否存在
        User existUser = userMapper.selectByUsername(username);
    return existUser;
    }

    @Override
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        //查询当前用户
        User dbUser = userMapper.selectById(userId);
        if(dbUser == null){
            throw new BusinessException(404,"用户不存在");
        }
        //2.验旧密码:matches(明文，哈希)
        if(!passwordEncoder.matches(oldPassword,dbUser.getPassword())){
            throw new BusinessException(400,"原密码错误");
        }
        //3.更新代码
        userMapper.updatePassword(userId,passwordEncoder.encode(newPassword));


    }
    //更改昵称
    @Override
    public void updateNickname(Long userId, String nickname) {
        userMapper.updateNickname(userId,nickname);
    }

    //更改头像
    @Override
    public void updateAvatar(Long currentUserId, String avatarUrl) {
        userMapper.updateAvatar(currentUserId,avatarUrl);
    }

    @Override
    public UserInfoVO getUserInfoVO(Long userId) {
        User user = userMapper.selectById(userId);
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfoVO);
        return userInfoVO;
    }

}
