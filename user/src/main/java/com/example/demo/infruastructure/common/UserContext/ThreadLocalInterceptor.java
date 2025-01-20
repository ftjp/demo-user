package com.example.demo.infruastructure.common.UserContext;

import cn.hutool.core.util.StrUtil;
import com.example.demo.infruastructure.util.DataRedisOptionService;
import com.example.demo.user.auth.ResidConstanst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author LJP
 * @date 2024/11/29 9:47
 */
@Slf4j
@Component
public class ThreadLocalInterceptor implements HandlerInterceptor {

    @Resource
    private DataRedisOptionService dataRedisOptionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerInterceptor.super.preHandle(request, response, handler);

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("bearer ")) {
            return true;
        }

        String accessToken = authHeader.substring(7);
        if (StrUtil.isBlank(accessToken)) {
            return true;
        }
        String userName = (String) dataRedisOptionService.get(ResidConstanst.ACCESS_TOKEN_PREFIX + accessToken);
        if (userName == null) {
            return true;
        }
        UserInfoContext userContext = (UserInfoContext) dataRedisOptionService.get(ResidConstanst.USER_CONTEXT_PREFIX + userName);
        LocalUserContext.setUserInfo(userContext);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        LocalUserContext.removeUserInfo();
    }
}
