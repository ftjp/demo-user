package com.example.demo.demo.database.controller;

import com.example.demo.demo.database.service.es.ElasticsearchTestService;
import com.example.demo.infruastructure.common.BaseResult;
import com.example.demo.user.dto.UserInfoDto;
import com.example.demo.user.param.UserInfoParam;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;


@RestController
@RequestMapping("/user/test")
@Api(tags = "test")
@Slf4j
public class EsTestController {


    @Resource
    private ElasticsearchTestService elasticsearchTestService;

    @PostMapping("/testEs/test")
    public BaseResult<String> test() throws IOException {
        log.info("test1接口");
        elasticsearchTestService.testEs();
        BaseResult<String> result = new BaseResult<>();
        result.setSuccess();
        return result;
    }

    @PostMapping("/testEs/testSearch")
    public BaseResult<String> testSearch() throws IOException {
        log.info("test1接口");
        elasticsearchTestService.testSearch();
        BaseResult<String> result = new BaseResult<>();
        result.setSuccess();
        return result;
    }


    @PostMapping("/testEs/insertTest")
    public BaseResult<UserInfoDto> insertTest(@RequestBody UserInfoParam param) {
        log.info("testEs 接口,param={}", param);
        elasticsearchTestService.insertTest(param);
        BaseResult<UserInfoDto> result = new BaseResult<>();
        result.setSuccess();
        return result;
    }

    @PostMapping("/testEs/findByIdTest")
    public BaseResult<UserInfoDto> findByIdTest(@RequestBody UserInfoParam param) {
        log.info("testEs 接口,param={}", param);
        UserInfoDto userInfoDto = elasticsearchTestService.findByIdTest();
        BaseResult<UserInfoDto> result = new BaseResult<>();
        result.setData(userInfoDto);
        result.setSuccess();
        return result;
    }

    @PostMapping("/testEs/findByParamTest")
    public BaseResult<UserInfoDto> findByParamTest(@RequestBody UserInfoParam param) {
        log.info("testEs 接口,param={}", param);
        elasticsearchTestService.findByParamTest();
        BaseResult<UserInfoDto> result = new BaseResult<>();
        result.setSuccess();
        return result;
    }


}
