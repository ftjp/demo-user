package com.example.demo.auth;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.example.demo.infruastructure.util.DataRedisOptionService;
import com.example.demo.infruastructure.util.ExceptionUtil;
import com.example.demo.user.domain.UserDomainService;
import com.example.demo.user.domain.entity.Role;
import com.example.demo.user.domain.entity.UserInfo;
import com.example.demo.user.dto.UserInfoDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    @Resource
    private DataRedisOptionService dataRedisOptionService;

    @Resource
    private UserDomainService userDomainService;

    private static final Map<String, UserInfo> userInfoMap = new HashMap<>();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!userInfoMap.containsKey(username)) {
            UserInfoDto userInfoDto = userDomainService.getUserInfoByName(username);
            ExceptionUtil.throwIfTrue(userInfoDto == null, "用户不存在");
            userInfoMap.put(userInfoDto.getUserName(), BeanUtil.copyProperties(userInfoDto, UserInfo.class));
        }
        UserInfo userInfo = userInfoMap.get(username);
        return new org.springframework.security.core.userdetails.User(
                userInfo.getUserName(),
                userInfo.getUserPwd(),
                getAuthorities(CollUtil.newArrayList(new Role().setRoleName("ADMIN")))
        );
    }

    public UserDetails getByAccessToken(String accessToken) throws UsernameNotFoundException {
        UserInfo userInfo = (UserInfo) dataRedisOptionService.get(accessToken);
        return new org.springframework.security.core.userdetails.User(
                userInfo.getUserName(),
                userInfo.getUserPwd(),
                getAuthorities(CollUtil.newArrayList(new Role().setRoleName("ADMIN")))
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(List<Role> roleList) {
        List<SimpleGrantedAuthority> authorities = roleList.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
                .collect(Collectors.toList());
        return authorities;
    }

}