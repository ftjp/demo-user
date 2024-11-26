package com.example.demo.user.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.user.domain.entity.Role;
import com.example.demo.user.domain.entity.Role;

import java.util.List;

/**
 * @author LJP
 * @date 2024/11/26 9:52
 */
public interface RoleService extends IService<Role> {

    Page<Role> findPage(Role params);

    List<Role> findList(Role params);

    Role findById(Long id);

    boolean insert(Role Role);

    boolean update(Role Role);

    int delete(Long id);

}