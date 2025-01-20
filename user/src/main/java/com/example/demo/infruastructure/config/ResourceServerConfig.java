package com.example.demo.infruastructure.config;

import com.example.demo.user.auth.filter.TokenRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * @author LJP
 * @date 2024/11/28 14:49
 */
@Configuration
@EnableAuthorizationServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Resource
    private TokenRequestFilter tokenRequestFilter;
    @Resource
    private PermitUrlCollector permitUrlCollector;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        // 配置资源ID，与授权服务器中的配置相对应
        resources.resourceId("user").tokenServices(tokenServices());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //禁用跨域保护
        http.csrf().disable();
        http.authorizeRequests()
                .regexMatchers(permitUrlCollector.getPermitUrls().toArray(new String[0])).permitAll()
                .antMatchers("/user/login").permitAll()
                .antMatchers("/user/test/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterAfter(tokenRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


    @Bean
    public ResourceServerTokenServices tokenServices() {
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setCheckTokenEndpointUrl("http://localhost:8081/oauth/check_token");
        tokenServices.setClientId("user");
        tokenServices.setClientSecret("user");
        return tokenServices;
    }

}