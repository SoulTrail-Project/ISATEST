package com.sixth.soul_trail.interceptor;

import com.sixth.soul_trail.utils.JwtUtil;
import com.sixth.soul_trail.utils.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            response.setStatus(401);
            response.getWriter().write("未登录");
            return false;
        }
        String token = auth.substring(7);
        Long userId = JwtUtil.parseToken(token);
        System.out.println("拦截器解析出来userId：" + userId);
        if (userId == null) {
            response.setStatus(401);
            response.getWriter().write("token 无效或已过期");
            return false;
        }
        request.setAttribute("userId", userId);
        SecurityUtil.setCurrentUserId(userId);   // 新增：写入 ThreadLocal
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        SecurityUtil.clear();   // 新增：清 ThreadLocal，防线程复用串号
    }
}
