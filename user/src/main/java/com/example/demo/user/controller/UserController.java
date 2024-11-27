package com.example.demo.user.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.config.Permit;
import com.example.demo.infruastructure.common.BaseResult;
import com.example.demo.user.domain.UserDomainService;
import com.example.demo.user.dto.UserInfoDto;
import com.example.demo.user.param.UserInfoParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author LJP
 * @date 2024/11/26 9:57
 */
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    @Resource
    private UserDomainService userDomainService;

    @PostMapping("/getExtendUserInfoById")
    public BaseResult<UserInfoDto> getExtendUserInfo(@RequestBody UserInfoParam param) {
        log.info("getExtendUserInfo接口，param :{}", JSON.toJSONString(param));
        BaseResult<UserInfoDto> result = new BaseResult<>();
        UserInfoDto extendUserInfo = userDomainService.getExtendUserInfoById(param.getId());
        result.setData(extendUserInfo);
        return result;
    }

    @Permit
    @PostMapping("/getExtendUserInfoByName")
    public BaseResult<UserInfoDto> getExtendUserInfoByName(@RequestBody UserInfoParam param) {
        log.info("getExtendUserInfo接口，param :{}", JSON.toJSONString(param));
        BaseResult<UserInfoDto> result = new BaseResult<>();
        UserInfoDto extendUserInfo = userDomainService.getExtendUserInfoByName(param.getUserName());
        result.setData(extendUserInfo);
        return result;
    }


}