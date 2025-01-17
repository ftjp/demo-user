package com.example.demo.auth;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.demo.infruastructure.common.BaseResult;
import com.example.demo.infruastructure.util.DataRedisOptionService;
import com.example.demo.infruastructure.util.ExceptionUtil;
import com.example.demo.infruastructure.util.StringRedisOptionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LJP
 * @date 2024/11/27 15:21
 */
@Service
public class AuthServiceClient {


    @Resource
    private DataRedisOptionService dataRedisOptionService;
    @Resource
    private StringRedisOptionService stringRedisOptionService;


    private final String AUTH_SERVICE_URL = "http://localhost:8081";

    public String requestAccessToken(String username, String password) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("grant_type", "password");
        paramMap.put("username", username);
        paramMap.put("password", password);
        paramMap.put("client_id", "user");
        paramMap.put("client_secret", "user");
        String post = HttpUtil.post(AUTH_SERVICE_URL + "/oauth/token", paramMap);

        JSONObject jsonObject = JSONUtil.parseObj(post);
        return jsonObject.get("access_token").toString();
    }

    public BaseResult revokeToken(String accessToken) {
        String result = HttpRequest.post(AUTH_SERVICE_URL + "/oauth/revokeToken").header("Authorization", "bearer " + accessToken).execute().body();
        return BeanUtil.toBean(result, BaseResult.class);
    }


    private String refreshAccessToken(String username, String refreshToken) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("grant_type", "refresh_token");
        paramMap.put("username", username);
        paramMap.put("refresh_token", refreshToken);
        paramMap.put("client_id", "user");
        paramMap.put("client_secret", "user");
        String post = HttpUtil.post(AUTH_SERVICE_URL + "/oauth/token", paramMap);
        JSONObject jsonObject = JSONUtil.parseObj(post);
        return jsonObject.get("access_token").toString();
    }

    public String refreshAccessToken(String accessToken) {
        ExceptionUtil.throwIfTrue(StrUtil.isBlank(accessToken), "无效的token");
        String userName = (String) dataRedisOptionService.get(ResidConstanst.ACCESS_TOKEN_PREFIX + accessToken);
        String realAccessToken = accessToken.replace("bearer ", "").trim();
//        String refreshToken = (String) dataRedisOptionService.get(ResidConstanst.ACCESS_TO_REFRESH_PREFIX + realAccessToken);
        // 由于token存的序列化问题，只能用 stringRedisOptionService
        String refreshToken = stringRedisOptionService.get(ResidConstanst.ACCESS_TO_REFRESH_PREFIX + realAccessToken);
        ExceptionUtil.throwIfTrue(StrUtil.isBlank(userName), "无效的token");
        String newAccessToken = refreshAccessToken(userName, refreshToken);
        dataRedisOptionService.set(ResidConstanst.ACCESS_TOKEN_PREFIX + newAccessToken, userName);
        return newAccessToken;
    }


}
