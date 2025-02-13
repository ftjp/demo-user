package com.example.demo.demo.database.service.es;

import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author LJP
 * @date 2025/2/12 11:28
 */
//@Repository
public interface UserRepository extends ElasticsearchRepository<UserInfoEs, Long> {

    List<UserInfoEs> findByUserName(String userName);

    List<UserInfoEs> findByCreateTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    @Highlight(fields = {@HighlightField(name = "userName"), @HighlightField(name = "userPwd")})
    List<UserInfoEs> findByUserNameOrUserPwd(String userName, String userPwd);


}