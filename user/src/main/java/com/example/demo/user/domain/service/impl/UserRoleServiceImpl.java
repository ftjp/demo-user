package com.example.demo.user.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.user.domain.entity.UserRole;
import com.example.demo.user.domain.mapper.UserRoleMapper;
import com.example.demo.user.domain.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author LJP
 * @date 2024/11/26 9:53
 */
@Service
@Slf4j
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Autowired
    private UserRoleMapper UserRoleMapper;

    @Override
    public Page<UserRole> findPage(UserRole params) {
        Page<UserRole> page = new Page<>(1, 10);
        LambdaQueryWrapper<UserRole> query = Wrappers.lambdaQuery(UserRole.class);
        return UserRoleMapper.selectPage(page, query);
    }

    @Override
    public List<UserRole> findList(UserRole params) {
        LambdaQueryWrapper<UserRole> query = Wrappers.lambdaQuery(UserRole.class);
        return UserRoleMapper.selectList(query);
    }

    @Override
    public UserRole findById(Long id) {
        return UserRoleMapper.selectById(id);
    }

    @Override
    public boolean insert(UserRole UserRole) {
        return save(UserRole);
    }

    @Override
    public boolean update(UserRole UserRole) {
        return updateById(UserRole);
    }

    @Override
    public int delete(Long id) {
        return UserRoleMapper.deleteById(id);
    }

    @Override
    public List<UserRole> findByUserId(Long userId) {
        return baseMapper.selectList(Wrappers.lambdaQuery(UserRole.class).eq(UserRole::getUserId, userId));
    }

    @Override
    public List<UserRole> findByRoleId(Long roleId) {
        return baseMapper.selectList(Wrappers.lambdaQuery(UserRole.class).eq(UserRole::getRoleId, roleId));
    }
}