package com.yxk.interceptor;

import com.yxk.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT 拦截器：拦截请求，校验 Token
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 从 header 里获取 token
        String header = request.getHeader(jwtUtils.getHeader());

        if (header == null || !header.startsWith(jwtUtils.getTokenPrefix())) {
            throw new RuntimeException("缺少或非法的 Token");
        }

        // 去掉前缀（Bearer ）
        String token = header.replace(jwtUtils.getTokenPrefix(), "").trim();

        // 校验 token
        Long currentUserId = jwtUtils.getUserId(token);
        if (currentUserId == null) {
            throw new RuntimeException("Token 无效或已过期");
        }

        // 把用户信息放入 request，方便后续 Controller 使用
        request.setAttribute("currentUserId", currentUserId);

        return true; // 放行
    }
}

