package com.example.demo.user.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.user.domain.entity.UserRole;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LJP
 * @date 2024/11/26 9:52
 */
public interface UserRoleService extends IService<UserRole> {

    Page<UserRole> findPage(UserRole params);

    List<UserRole> findList(UserRole params);

    UserRole findById(Long id);

    boolean insert(UserRole UserRole);

    boolean update(UserRole UserRole);

    int delete(Long id);

    List<UserRole> findByUserId(Long userId);

    List<UserRole> findByRoleId(Long roleId);

}