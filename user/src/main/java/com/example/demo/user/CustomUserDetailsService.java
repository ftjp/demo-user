package com.example.demo.user;

import com.example.demo.infruastructure.exception.BaseCustomException;
import com.google.common.collect.Lists;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Map<String, UserInfo> userInfoMap = new HashMap<>();

    static {
//        userInfoMap.put("admin", new UserInfo().setUsername("admin").setPassword("$2a$10$FRy27hJLMoKYp0ZgpTWRmeqQvQAxuSioP71oEvjY.7qL0WQqk9sfK").setRoles(Lists.newArrayList(new Role().setRoleName("ADMIN"))));
        userInfoMap.put("admin", new UserInfo().setUsername("admin").setPassword("123456").setRoles(Lists.newArrayList(new Role().setRoleName("ADMIN"))));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!userInfoMap.containsKey(username)) {
            throw new BaseCustomException("用户不存在");
        }
        UserInfo userInfo = userInfoMap.get(username);
        return new org.springframework.security.core.userdetails.User(
                userInfo.getUsername(),
                userInfo.getPassword(),
                getAuthorities(userInfo)
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(UserInfo userInfo) {
        List<SimpleGrantedAuthority> authorities = userInfo.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
                .collect(Collectors.toList());
        return authorities;
    }

}