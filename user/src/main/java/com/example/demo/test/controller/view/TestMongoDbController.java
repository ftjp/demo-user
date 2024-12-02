package com.example.demo.test.controller.view;

import com.example.demo.infruastructure.common.BaseResult;
import com.example.demo.test.service.MongoDbTestService;
import com.example.demo.test.service.TestService;
import com.example.demo.user.dto.UserInfoDto;
import com.example.demo.user.param.UserInfoParam;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/user/test")
@Api(tags = "test")
@Slf4j
public class TestMongoDbController {

    @Resource
    private MongoDbTestService mongoDbTestService;

    @PostMapping("/testMongoDb/insertTest")
    public BaseResult<UserInfoDto> testMongoDb(@RequestBody UserInfoParam param) {
        log.info("testMongoDb接口,param={}", param);
        BaseResult<UserInfoDto> result = new BaseResult<>();
        result.setSuccess();
        UserInfoDto userInfoDto = mongoDbTestService.insertTest(param);
        result.setData(userInfoDto);
        return result;
    }

    @PostMapping("/testMongoDb/insertAllTest")
    public BaseResult<List<UserInfoDto>> testMongoDb(@RequestBody List<UserInfoParam> paramList) {
        log.info("testMongoDb接口,paramList={}", paramList);
        BaseResult<List<UserInfoDto>> result = new BaseResult<>();
        result.setSuccess();
        List<UserInfoDto> userInfoDtoList = mongoDbTestService.insertAllTest(paramList);
        result.setData(userInfoDtoList);
        return result;
    }

    @PostMapping("/testMongoDb/findByUsername")
    public BaseResult<UserInfoDto> findByUsername() {
        log.info("testMongoDb接口");
        BaseResult<UserInfoDto> result = new BaseResult<>();
        result.setSuccess();
        UserInfoDto userInfoDto = mongoDbTestService.findByUsername();
        List<UserInfoDto> userInfoDtoList = mongoDbTestService.findByExample();
        result.setData(userInfoDto);
        return result;
    }


}
