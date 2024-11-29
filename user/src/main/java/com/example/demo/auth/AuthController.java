package com.example.demo.auth;

import com.example.demo.config.Permit;
import com.example.demo.filter.JwtTokenUtil;
import com.example.demo.infruastructure.common.BaseResult;
import com.example.demo.infruastructure.util.DataRedisOptionService;
import com.example.demo.infruastructure.util.ExceptionUtil;
import com.example.demo.user.domain.UserDomainService;
import com.example.demo.user.domain.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    @Resource
    private UserDomainService userDomainService;

    @Permit
    @PostMapping("/user/login")
    public void login(@RequestBody UserInfo userInfo, HttpServletResponse response) {
        userDomainService.buildUserContext(userInfo.getUserName());
        String accessToken = authServiceClient.requestAccessToken(userInfo.getUserName(), userInfo.getUserPwd());
        dataRedisOptionService.set(ResidConstanst.ACCESS_TOKEN_PREFIX + accessToken, userInfo.getUserName());
        // 设置响应头
        response.addHeader("Authorization", "bearer " + accessToken);
        // 设置响应状态码
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @PostMapping("/user/loginOut")
    public BaseResult loginOut(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        ExceptionUtil.throwIfTrue(authHeader == null || !authHeader.startsWith("bearer "), "无效的token");
        String accessToken = authHeader.substring(7);
        return authServiceClient.revokeToken(accessToken);
    }

    @Permit
    @PostMapping("/user/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader("Authorization");
        ExceptionUtil.throwIfTrue(authHeader == null || !authHeader.startsWith("bearer "), "无效的token");
        String accessToken = authHeader.substring(7);
        String newAccessToken = authServiceClient.refreshAccessToken(accessToken);
        // 设置响应头
        response.addHeader("Authorization", "bearer " + newAccessToken);
        // 设置响应状态码
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
