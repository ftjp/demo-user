package com.example.demo.auth;

import com.example.demo.infruastructure.util.TokenUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private TokenUtil tokenUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 从authentication中获取用户名
        String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        // 生成JWT Token
        String token = tokenUtil.generateToken(username);
        // 设置响应头
        response.addHeader("Authorization", "Bearer " + token);
        // 设置响应状态码
        response.setStatus(HttpServletResponse.SC_OK);
    }


}