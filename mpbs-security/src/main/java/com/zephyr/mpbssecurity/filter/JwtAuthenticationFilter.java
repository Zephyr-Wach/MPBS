package com.zephyr.mpbssecurity.filter;

import com.zephyr.mpbscommon.utils.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 排除 /public/** 路径，不做 token 验证
//        String path = request.getRequestURI();
//        if (path.startsWith("/public/")) {
//            filterChain.doFilter(request, response);
//            return;
//        }

        String token = request.getHeader("Authorization");

        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);

                String userId = JwtUtil.getUserIdFromToken(token);
                String role = JwtUtil.getRoleFromToken(token);

                if (userId != null && role != null) {
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, Collections.singletonList(authority));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            // token 解析异常，打印日志，清空认证，继续放行请求
            logger.warn("JWT解析失败: " + e.getMessage());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
