package com.example.demo.auth;

import com.example.demo.infruastructure.util.TokenUtil;
import com.example.demo.user.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class AuthController {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private TokenUtil tokenUtil;

    // 好像没啥用，请求不会走进来
    @PostMapping("/login")
    public String createAuthenticationToken(@RequestBody UserInfo userInfo) throws Exception {
        authenticate(userInfo.getUsername(), userInfo.getPassword());
        // 获得认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 生成token
        String jwtToken = tokenUtil.generateToken(authentication);

        log.info("jwtToken: {}", jwtToken);

        return "/test.html";
    }

    private void authenticate(String username, String password) throws Exception {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

}
