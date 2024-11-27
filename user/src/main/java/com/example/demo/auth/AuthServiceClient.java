package com.example.demo.auth;


import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LJP
 * @date 2024/11/27 15:21
 */
@Service
public class AuthServiceClient {

    private final String AUTH_SERVICE_URL = "http://localhost:8081";

    public String requestAccessToken(String username, String password) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("grant_type", "password");
        paramMap.put("username", username);
        paramMap.put("password", password);
        paramMap.put("client_id", "user");
        paramMap.put("client_secret", "123456");
        paramMap.put("scope", "all");
        String post = HttpUtil.post(AUTH_SERVICE_URL + "/oauth/token", paramMap);

        JSONObject jsonObject = JSONUtil.parseObj(post);
        return jsonObject.get("access_token").toString();
    }
}
