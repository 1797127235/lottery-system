package org.lotterysystem.common.interceptor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.lotterysystem.common.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    private JwtUtil jwtUtil;
    /*
        请求之前调用
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取 token，支持 Authorization: Bearer xxx 或自定义 user_token
        String authHeader = request.getHeader("Authorization");
        String userToken = request.getHeader("user_token");

        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        } else if (userToken != null && !userToken.isBlank()) {
            token = userToken.trim();
        }

        if (token == null || token.isBlank()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未登录或token缺失\"}");
            return false;
        }

        try {
            Claims claims = jwtUtil.parseJwt(token);
            // 可放入 request 供后续业务使用
            request.setAttribute("jwtClaims", claims);
            request.setAttribute("jwtUserId", claims.getSubject());
            request.setAttribute("jwtIdentity", claims.get("identity"));
            return true;
        } catch (JwtException e) {
            log.warn("Token 校验失败: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"token无效或已过期\"}");
            return false;
        }

    }
}
