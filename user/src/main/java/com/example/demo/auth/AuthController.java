package com.example.demo.auth;

import com.example.demo.config.Permit;
import com.example.demo.filter.JwtTokenUtil;
import com.example.demo.infruastructure.util.DataRedisOptionService;
import com.example.demo.user.domain.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
public class AuthController {

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private AuthServiceClient authServiceClient;

    @Resource
    private DataRedisOptionService dataRedisOptionService;


    @Permit
    @PostMapping("/user/login")
    public void login(@RequestBody UserInfo userInfo, HttpServletResponse response) throws Exception {

        String accessToken = authServiceClient.requestAccessToken(userInfo.getUserName(), userInfo.getUserPwd());
        dataRedisOptionService.set(accessToken, userInfo);
        String token = jwtTokenUtil.generateToken(accessToken);
        // 设置响应头
        response.addHeader("Authorization", "bearer " + token);
        // 设置响应状态码
        response.setStatus(HttpServletResponse.SC_OK);
    }


}
