package com.example.demo.demo.database.service;

import com.example.demo.infruastructure.util.IdGenerator;
import com.example.demo.user.domain.UserDomainService;
import com.example.demo.user.dto.UserInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author LJP
 * @date 2024/11/29 17:26
 */
@Slf4j
@Service
public class MysqlTestService {

    @Resource
    private IdGenerator idGenerator;

    @Resource
    private UserDomainService userDomainService;


    public UserInfoDto findByUsername(String userName) {
        UserInfoDto userInfoByName = userDomainService.getUserInfoByName(userName);
        return userInfoByName;
    }


}
