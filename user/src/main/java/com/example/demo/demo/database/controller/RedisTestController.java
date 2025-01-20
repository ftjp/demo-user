package com.example.demo.demo.database.controller;

import com.example.demo.demo.database.service.RedisTestService;
import com.example.demo.infruastructure.common.BaseResult;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/user/test")
@Api(tags = "test")
@Slf4j
public class RedisTestController {


    @Resource
    private RedisTestService redisTestService;

    @GetMapping("/testRedis/test")
    public BaseResult<String> test() {
        log.info("test1接口");
        BaseResult<String> result = new BaseResult<>();
        result.setSuccess();
        String res = redisTestService.testRedis();
        result.setData(res);
        return result;
    }


}
