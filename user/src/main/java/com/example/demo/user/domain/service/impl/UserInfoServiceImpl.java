package com.example.demo.user.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.user.domain.entity.UserInfo;
import com.example.demo.user.domain.mapper.UserInfoMapper;
import com.example.demo.user.domain.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LJP
 * @date 2024/11/26 9:53
 */
@Service
@Slf4j
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public Page<UserInfo> findPage(UserInfo params) {
        Page<UserInfo> page = new Page<>(1, 10);
        LambdaQueryWrapper<UserInfo> query = Wrappers.lambdaQuery(UserInfo.class);
        return userInfoMapper.selectPage(page, query);
    }

    @Override
    public List<UserInfo> findList(UserInfo params) {
        LambdaQueryWrapper<UserInfo> query = Wrappers.lambdaQuery(UserInfo.class);
        return userInfoMapper.selectList(query);
    }

    @Override
    public UserInfo findById(Long id) {
        return userInfoMapper.selectById(id);
    }

    @Override
    public UserInfo findByName(String name) {
        return baseMapper.selectOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getUserName, name).last("limit 1"));
    }

    @Override
    public boolean insert(UserInfo userInfo) {
        return save(userInfo);
    }

    @Override
    public boolean update(UserInfo userInfo) {
        return updateById(userInfo);
    }

    @Override
    public int delete(Long id) {
        return userInfoMapper.deleteById(id);
    }

}