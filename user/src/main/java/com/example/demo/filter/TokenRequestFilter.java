package com.example.demo.filter;

import cn.hutool.core.util.StrUtil;
import com.example.demo.auth.CustomUserDetailsService;
import com.example.demo.infruastructure.util.DataRedisOptionService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TokenRequestFilter extends OncePerRequestFilter {

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private CustomUserDetailsService customUserDetailsService;

    @Resource
    private DataRedisOptionService dataRedisOptionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("bearer ")) {
            String token = authHeader.substring(7);
            if (StrUtil.isNotBlank(token) && jwtTokenUtil.validateToken(token)) {
                String accessToken = jwtTokenUtil.getAccessTokenFromJWT(token);
                UserDetails userDetails = customUserDetailsService.getByAccessToken(accessToken);
                if (userDetails != null) {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }


}