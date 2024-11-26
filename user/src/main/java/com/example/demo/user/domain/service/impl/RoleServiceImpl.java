package com.example.demo.user.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.user.domain.entity.Role;
import com.example.demo.user.domain.mapper.RoleMapper;
import com.example.demo.user.domain.service.RoleService;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper RoleMapper;

    @Override
    public Page<Role> findPage(Role params) {
        Page<Role> page = new Page<>(1, 10);
        LambdaQueryWrapper<Role> query = Wrappers.lambdaQuery(Role.class);
        return RoleMapper.selectPage(page, query);
    }

    @Override
    public List<Role> findList(Role params) {
        LambdaQueryWrapper<Role> query = Wrappers.lambdaQuery(Role.class);
        return RoleMapper.selectList(query);
    }

    @Override
    public Role findById(Long id) {
        return RoleMapper.selectById(id);
    }

    @Override
    public boolean insert(Role Role) {
        return save(Role);
    }

    @Override
    public boolean update(Role Role) {
        return updateById(Role);
    }

    @Override
    public int delete(Long id) {
        return RoleMapper.deleteById(id);
    }

}