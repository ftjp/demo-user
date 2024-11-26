package com.example.demo.user.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.user.domain.entity.UserInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LJP
 * @date 2024/11/26 9:52
 */
public interface UserInfoService extends IService<UserInfo> {

    Page<UserInfo> findPage(UserInfo params);

    List<UserInfo> findList(UserInfo params);

    UserInfo findById(Long id);

    UserInfo findByName(String name);

    boolean insert(UserInfo userInfo);

    boolean update(UserInfo userInfo);

    int delete(Long id);

}