package com.sixth.soul_trail.controller;

import com.sixth.soul_trail.VO.LoginVO;
import com.sixth.soul_trail.VO.UpdatePasswordRequestVO;
import com.sixth.soul_trail.VO.UpdateProfileRequestVO;
import com.sixth.soul_trail.VO.UserInfoVO;
import com.sixth.soul_trail.mapper.UserMapper;
import com.sixth.soul_trail.pojo.User;
import com.sixth.soul_trail.service.UserService;
import com.sixth.soul_trail.common.Result;
import com.sixth.soul_trail.utils.JwtUtil;
import com.sixth.soul_trail.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
//    @Autowired
//    private UserMapper userMapper;

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
        //登录接口本身不需要 token，直接按用户名查

//        Long userId = SecurityUtil.getCurrentUserId();
//        log.info("当前登录userId:{}",userId);
//// 如果你这里要查用户，用 selectById
//        User user = userMapper.selectById(userId);

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

        String token = JwtUtil.genToken(dbUser.getId());
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
        vo.setUserInfo(info);
        return new Result<>(200,"登录成功",vo);
    }
    //修改密码
    @PostMapping("/password")
    public Result password(@RequestBody UpdatePasswordRequestVO req) {
        //1.检验密码格式正确
        if (req.getOldPassword() == null || req.getNewPassword() == null) {
            return Result.error(400, "原密码、新密码不能为空");
        }
        if (req.getConfirmPassword() != null
                && !req.getNewPassword().equals(req.getConfirmPassword())) {
            return Result.error(400, "两次输入的新密码不一致");
        }
        if (req.getNewPassword().length() < 6 || req.getNewPassword().length() > 20) {
            return Result.error(400, "新密码长度需在 6~20 位之间");
        }
        //2.
        Long userId = SecurityUtil.getCurrentUserId();
        userService.updatePassword(userId,req.getOldPassword(),req.getNewPassword());
                //改完密码，前端须引导重新登录
        return Result.success("密码修改成功，请重新登陆");
    }
    //修改昵称
    @PutMapping("/profile")
    public Result profile(@RequestBody UpdateProfileRequestVO req) {
        if(req.getNickname()==null || req.getNickname().trim().isEmpty()) {
            return Result.error(400,"昵称不能为空");
        }

        if(req.getNickname().length() > 20) {
            return Result.error(400,"昵称最长20个字符");
        }
        Long userId = SecurityUtil.getCurrentUserId();
        userService.updateNickname(userId,req.getNickname().trim());
        return Result.success("昵称修改成功");
    }
    // ============ 上传头像 ============
    @PostMapping("/avatar")
    public Result updateAvatar(@RequestParam("file") MultipartFile file) {
        // 1.空文件
        if (file == null || file.isEmpty()) {
            return Result.error(400, "请选择头像文件");
        }
        // 2.后缀白名单
        String original = file.getOriginalFilename();
        String ext = (original != null && original.contains("."))
                ? original.substring(original.lastIndexOf('.')).toLowerCase() : "";
        if (!ext.equals(".jpg") && !ext.equals(".jpeg")
                && !ext.equals(".png") && !ext.equals(".webp")) {
            return Result.error(400, "只支持 jpg/png/webp 格式");
        }
        // 3.大小限制 2MB
        if (file.getSize() > 2 * 1024 * 1024) {
            return Result.error(400, "头像不能超过 2MB");
        }
        // 4.UUID 重命名防覆盖，存到项目根目录 uploads/avatars/
        String filename = UUID.randomUUID().toString().replace("-", "") + ext;
        try {
            Path dir = Paths.get("uploads", "avatars").toAbsolutePath();
            Files.createDirectories(dir);
            file.transferTo(dir.resolve(filename).toFile());
        } catch (IOException e) {
            log.error("头像保存失败", e);
            return Result.error(500, "头像上传失败");
        }
        // 5.落库的是相对 URL，前端拼上服务器地址即可访问
        String avatarUrl = "/uploads/avatars/" + filename;
        userService.updateAvatar(SecurityUtil.getCurrentUserId(), avatarUrl);
        return Result.success(avatarUrl);
    }

    @GetMapping("/info")
    public Result<UserInfoVO> getUserInfo() {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        UserInfoVO userInfoVO = userService.getUserInfoVO(currentUserId);
        return Result.success(userInfoVO);
    }



}
