package com.sixth.soul_trail.controller;

import com.sixth.soul_trail.VO.LoginVO;
import com.sixth.soul_trail.VO.UserInfoVO;
import com.sixth.soul_trail.pojo.User;
import com.sixth.soul_trail.service.UserService;
import com.sixth.soul_trail.common.Result;
import com.sixth.soul_trail.utils.JwtUtil;
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
    * 鐢ㄦ埛娉ㄥ唽鎺ュ彛
    * */
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        // 鏍￠獙鐢ㄦ埛琛ㄥ崟鏄惁涓虹┖锛屼负绌烘墦鍥�
        if (user.getUsername() == null || user.getPassword() == null) return Result.error(400,"鐢ㄦ埛銆佸瘑鐮佷笉鑳戒负绌�");
        // 浜ょ粰service灞傝繘琛屾敞鍐屼笟鍔★紝骞惰繑鍥炵粨鏋�
        boolean success = userService.register(user);
        // 鏍规嵁缁撴灉杩涜澶勭悊
        // 鍓嶇璁板緱閲嶅畾鍚戝埌鐧诲綍鐣岄潰
        return success ? Result.success("娉ㄥ唽鎴愬姛锛岃鐧诲綍") : Result.error(400,"鐢ㄦ埛鍚嶅凡瀛樺湪");
    }
    /*
    * 鐢ㄦ埛鐧诲綍鎺ュ彛
     */
    @PostMapping("/login")
    public Result login(@RequestBody User loginUser) {
        //鍒ゆ柇鐢ㄦ埛鏄惁瀛樺湪
        User dbUser = userService.findByUserName(loginUser.getUsername());
        if  (dbUser == null) {
            return Result.error(400,"鐢ㄦ埛涓嶅瓨鍦�");
        }
        //鍒ゆ柇瀵嗙爜鏄惁姝ｇ‘
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(!passwordEncoder.matches(loginUser.getPassword(), dbUser.getPassword())) {
            return Result.error(400,"瀵嗙爜閿欒");
        }
        String token = JwtUtil.genToken(dbUser.getId());
        //鐢ㄦ埛淇℃伅
        UserInfoVO info = new UserInfoVO();
        info.setId(dbUser.getId());
        info.setUsername(dbUser.getUsername());
        info.setNickname(dbUser.getNickname());
        info.setAvatar(dbUser.getAvatarUrl());          // avatarUrl 鈫� avatar
        info.setCreatedAt(dbUser.getCreatedAt());
        //鐧诲綍淇℃伅
        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setExpiresIn(JwtUtil.EXPIRE_SECONDS);
        vo.setUserInfo(info);//
        return new Result<>(200,"鐧诲綍鎴愬姛",vo);
    }

}
