package com.yxk.interceptor;

import com.yxk.utils.JwtUtils;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * JWT 拦截器：拦截请求，校验 Token
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true; // 预检请求直接放行
        }

        // 从 header 里获取 token
        String header = request.getHeader(jwtUtils.getHeader());

        System.out.println("=== 打印所有请求头 ===");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + " : " + value);
        }

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

