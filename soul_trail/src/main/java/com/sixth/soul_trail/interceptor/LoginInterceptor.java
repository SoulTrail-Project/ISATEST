package com.sixth.soul_trail.interceptor;

import com.sixth.soul_trail.utils.JwtUtil;
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
        // 从请求头取 token
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            response.setStatus(401);
            response.getWriter().write("未登录");
            return false;
        }
        String token = auth.substring(7);            // 去掉 "Bearer "
        Long userId = JwtUtil.parseToken(token);
        if (userId == null) {
            response.setStatus(401);
            response.getWriter().write("token 无效或已过期");
            return false;
        }
        // 把 userId 传给后续 Controller（需要时取）
        request.setAttribute("userId", userId);
        return true;
    }
}
