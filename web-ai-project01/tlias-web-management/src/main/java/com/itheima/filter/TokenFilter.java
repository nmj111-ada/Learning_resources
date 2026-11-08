package com.itheima.filter;

import com.itheima.util.CurrentHolder;
import com.itheima.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/** 请求先经过此过滤器：校验 token 请求头，再把操作人交给后续 Controller/AOP。 */
@Component
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;

    // 登录接口无需令牌；静态页面由现有 Nginx 提供，其他后端接口均校验登录。
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return "/login".equals(request.getServletPath()) || "OPTIONS".equals(request.getMethod());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 仅捕获令牌解析异常，业务代码的异常继续交给原有异常处理流程。
        Integer empId;
        try {
            empId = jwtUtils.parseEmployeeId(request.getHeader("token"));
        } catch (RuntimeException e) {
            // 前端 Axios 已约定收到 401 后跳转登录页。
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":0,\"msg\":\"NOT_LOGIN\",\"data\":null}");
            return;
        }
        try {
            CurrentHolder.setCurrentId(empId);
            filterChain.doFilter(request, response);
        } finally {
            // Tomcat 会复用线程，必须在 finally 中清理，包括业务抛异常的情况。
            CurrentHolder.remove();
        }
    }
}
