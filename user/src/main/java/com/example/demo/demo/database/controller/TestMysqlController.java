package com.example.demo.demo.database.controller;

import com.example.demo.demo.database.service.MysqlTestService;
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


@RestController
@RequestMapping("/user/test")
@Api(tags = "test")
@Slf4j
public class TestMysqlController {


    @Resource
    private MysqlTestService mysqlTestService;

    @PostMapping("/testMysql/findByUsername")
    public BaseResult<UserInfoDto> findByUsername(@RequestBody UserInfoParam param) {
        log.info("testMysql接口");
        BaseResult<UserInfoDto> result = new BaseResult<>();
        result.setSuccess();
        UserInfoDto userInfoDto = mysqlTestService.findByUsername(param.getUserName());
        result.setData(userInfoDto);
        return result;
    }


}
