package com.example.demo.test.service;

import com.example.demo.infruastructure.util.DataRedisOptionService;
import com.example.demo.infruastructure.util.DataRedisOptionUtil;
import com.example.demo.user.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author caili
 * @create 2024/11/18 17:58
 */
@Service
@Slf4j
public class TestService {

    @Resource
    private DataRedisOptionUtil dataRedisOptionUtil;
    @Resource
    private DataRedisOptionService dataRedisOptionService;

    public String testRedis() {
        dataRedisOptionUtil.tryLockKey("test", () -> {
            System.out.println("********************** test ******************");
        });
        // 保存字符串
        dataRedisOptionService.set("testSet", "testSet");
        String test = (String) dataRedisOptionService.get("test");
        // 保存对象
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("test");
        userInfo.setPassword("123");
        userInfo.setAge(12);
        userInfo.setEmail("test@qq.com");
        dataRedisOptionService.set("userInfo", userInfo);
        UserInfo userInfoFormRedis = (UserInfo) dataRedisOptionService.get("userInfo");
        System.out.println(userInfoFormRedis);
        dataRedisOptionService.del("testSet", "userInfo");
        return test;
    }


}
