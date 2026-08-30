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
        // 放行 CORS 预检请求（OPTIONS），否则前端跨域联调会失败
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未登录\",\"data\":null}");
            return false;
        }
        String token = auth.substring(7);
        Long userId = JwtUtil.parseToken(token);
        System.out.println("拦截器解析出来userId：" + userId);
        if (userId == null) {
            response.setStatus(401);                 // 提前到 write 之前，原来写反了
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"token无效或已过期\",\"data\":null}");
            return false;
        }
        request.setAttribute("userId", userId);
        SecurityUtil.setCurrentUserId(userId);   // 写入 ThreadLocal
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        SecurityUtil.clear();   // 清 ThreadLocal，防线程复用串号
    }
}
