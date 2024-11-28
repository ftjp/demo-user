package com.example.demo.filter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.example.demo.auth.ResidConstanst;
import com.example.demo.infruastructure.util.DataRedisOptionService;
import com.example.demo.infruastructure.util.ExceptionUtil;
import com.example.demo.user.domain.UserDomainService;
import com.example.demo.user.dto.RoleDto;
import com.example.demo.user.dto.UserInfoDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TokenRequestFilter extends OncePerRequestFilter {

    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private DataRedisOptionService dataRedisOptionService;
    @Resource
    private UserDomainService userDomainService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("bearer ")) {
            String token = authHeader.substring(7);
            if (StrUtil.isNotBlank(token) && jwtTokenUtil.validateToken(token)) {
                String accessToken = jwtTokenUtil.getAccessTokenFromJWT(token);
                UserDetails userDetails = getByAccessToken(accessToken);
                if (userDetails != null) {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }

    private UserDetails getByAccessToken(String accessToken) throws UsernameNotFoundException {
        String userName = (String) dataRedisOptionService.get(ResidConstanst.ACCESS_TOKEN_PREFIX + accessToken);
        ExceptionUtil.throwIfTrue(userName == null, "token 过期");
        UserInfoDto userInfoDto = userDomainService.getExtendUserInfoByName(userName);
        User user = new User(userInfoDto.getUserName(), userInfoDto.getUserPwd(),
                getAuthorities(userInfoDto.getRoleList()));
        dataRedisOptionService.set(ResidConstanst.USER_DETAILS_PREFIX + userName, user);
        return user;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(List<RoleDto> roleList) {
        if (CollUtil.isEmpty(roleList)) {
            return Collections.emptyList();
        }
        List<SimpleGrantedAuthority> authorities = roleList.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
                .collect(Collectors.toList());
        return authorities;
    }

}