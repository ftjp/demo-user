package com.example.demo.test.service;

import cn.hutool.core.bean.BeanUtil;
import com.example.demo.infruastructure.util.IdGenerator;
import com.example.demo.user.domain.UserDomainService;
import com.example.demo.user.domain.entity.UserInfo;
import com.example.demo.user.dto.UserInfoDto;
import com.example.demo.user.param.UserInfoParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
