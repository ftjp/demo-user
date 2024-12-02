package com.example.demo.test.service;

import cn.hutool.core.bean.BeanUtil;
import com.example.demo.infruastructure.common.UserContext.UserInfoContext;
import com.example.demo.infruastructure.util.IdGenerator;
import com.example.demo.user.domain.entity.UserInfo;
import com.example.demo.user.dto.UserInfoDto;
import com.example.demo.user.param.UserInfoParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * @author LJP
 * @date 2024/11/29 17:26
 */
@Slf4j
@Service
public class MongoDbTestService {

    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private IdGenerator idGenerator;

    public UserInfoDto insertTest(UserInfoParam param) {
        UserInfo userInfo = BeanUtil.copyProperties(param, UserInfo.class);
        userInfo.setId(idGenerator.nextId());
        mongoTemplate.insert(userInfo);
        return BeanUtil.copyProperties(userInfo, UserInfoDto.class);
    }

    public List<UserInfoDto> insertAllTest(List<UserInfoParam> paramList) {
        List<UserInfo> userInfoList = BeanUtil.copyToList(paramList, UserInfo.class);
        userInfoList.forEach(userInfo -> userInfo.setId(idGenerator.nextId()));
        mongoTemplate.insertAll(userInfoList);
        return BeanUtil.copyToList(userInfoList, UserInfoDto.class);
    }

    public UserInfoDto findByUsername() {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(1L));
        return mongoTemplate.findOne(query, UserInfoDto.class);
    }

    public List<UserInfoDto> findByExample() {
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setUserName("username");
        Query query = Query.query(Criteria.where("userName").is(userInfoDto.getUserName()));
        return mongoTemplate.find(query, UserInfoDto.class);
    }

}
