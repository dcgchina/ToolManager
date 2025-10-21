package com.example.demojwt.config;


import com.example.demojwt.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // 1. 从请求头中获取 Authorization
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 2. 检查是否有 Bearer Token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. 提取真正的 JWT（去掉 Bearer 前缀）
        jwt = authHeader.substring(7);

        try {
            // 4. 调用你的 JwtService 方法！！！（关键！）
            username = jwtService.vaildateTokenAndGetUsername(jwt);
        } catch (RuntimeException e) {
            // Token 无效，啥都不做，继续过滤链
            filterChain.doFilter(request, response);
            return;
        }

        // 5. 如果解析出了用户名，且当前没有认证信息
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 模拟一个已认证用户（可后续接入数据库查询权限）
            UserDetails userDetails = User.withUsername(username).password("").authorities("ROLE_ADMIN").build();

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 6. 👇 这一行是核心：告诉 Spring Security 当前是谁在访问
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // 7. 继续过滤器链（进入你的 Controller）
        filterChain.doFilter(request, response);
    }
}