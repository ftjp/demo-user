package com.example.demo.demo.database.service;

import cn.hutool.core.bean.BeanUtil;
import com.example.demo.infruastructure.util.IdGenerator;
import com.example.demo.user.domain.entity.UserInfo;
import com.example.demo.user.dto.UserInfoDto;
import com.example.demo.user.param.UserInfoParam;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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
        userInfo.setCreateTime(LocalDateTime.now());
        mongoTemplate.insert(userInfo);
        mongoTemplate.insert(userInfo, "userInfo2");
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
//        return mongoTemplate.findOne(query, UserInfoDto.class);
        UserInfo userInfo2 = mongoTemplate.findOne(query, UserInfo.class, "userInfo2");
        UserInfoParam userInfo3 = mongoTemplate.findOne(query, UserInfoParam.class, "userInfo2");
        return mongoTemplate.findOne(query, UserInfoDto.class, "userInfo2");
    }

    public List<UserInfoDto> findByExample() {
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setUserName("username");
        Query query = Query.query(Criteria.where("userName").is(userInfoDto.getUserName()));
        return mongoTemplate.find(query, UserInfoDto.class);
    }

    public void updateByExample() {
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setUserName("username");
        userInfoDto.setUserPwd("test");
        Query query = Query.query(Criteria.where("userName").is(userInfoDto.getUserName()));
        Update updateDefinition = new Update();
        updateDefinition.set("userPwd", userInfoDto.getUserPwd());
        // setOnInsert 插入时才生效
        updateDefinition.setOnInsert("test", 1);
        // addToSet set 集合
        updateDefinition.addToSet("addToSet", 0);
        updateDefinition.addToSet("addToSet", 0);
        updateDefinition.addToSet("addToSet", 1);
        // 删除
        updateDefinition.unset("updateTime");
        // 自增
        updateDefinition.inc("deleted");
        UpdateResult updateResult = mongoTemplate.updateMulti(query, updateDefinition, UserInfoDto.class);
        log.info("updateResult:{}", updateResult);
    }

}
