package com.example.demo.infruastructure.config;

import com.example.demo.user.auth.filter.TokenRequestFilter;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.annotation.Resource;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private TokenRequestFilter tokenRequestFilter;
    @Resource
    private PermitUrlCollector permitUrlCollector;

    //     会被覆盖
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        //禁用跨域保护
//        http.csrf().disable();
//        http.authorizeRequests()
//                .regexMatchers(permitUrlCollector.getPermitUrls().toArray(new String[0])).permitAll()
//                .antMatchers("/user/login").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .addFilterBefore(tokenRequestFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(tokenRequestFilter, UsernamePasswordAuthenticationFilter.class)
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        //忽略静态资源
        webSecurity.ignoring().antMatchers("/assents/**", "/login.html");
    }


}