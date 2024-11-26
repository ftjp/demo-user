package com.example.demo.user.domain;

import cn.hutool.core.bean.BeanUtil;
import com.example.demo.user.domain.entity.Role;
import com.example.demo.user.domain.entity.UserInfo;
import com.example.demo.user.domain.entity.UserRole;
import com.example.demo.user.domain.service.RoleService;
import com.example.demo.user.domain.service.UserInfoService;
import com.example.demo.user.domain.service.UserRoleService;
import com.example.demo.user.dto.RoleDto;
import com.example.demo.user.dto.UserInfoDto;
import com.example.demo.user.param.UserInfoParam;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LJP
 * @date 2024/11/26 10:23
 */
@Service
@Slf4j
public class UserDomainService {

    @Resource
    private UserInfoService userInfoService;
    @Resource
    private RoleService roleService;
    @Resource
    private UserRoleService userRoleService;


    public UserInfoDto getUserInfoById(Long userId) {
        UserInfo userInfo = userInfoService.findById(userId);
        return BeanUtil.copyProperties(userInfo, UserInfoDto.class);
    }

    public UserInfoDto getExtendUserInfoById(Long userId) {
        UserInfo userInfo = userInfoService.findById(userId);
        return getExtendUserInfo(userInfo);
    }

    public UserInfoDto getUserInfoByName(String userName) {
        UserInfo userInfo = userInfoService.findByName(userName);
        return BeanUtil.copyProperties(userInfo, UserInfoDto.class);
    }

    public UserInfoDto getExtendUserInfoByName(String userName) {
        UserInfo userInfo = userInfoService.findByName(userName);
        return getExtendUserInfo(userInfo);
    }

    public UserInfoDto getExtendUserInfo(UserInfo userInfo) {
        UserInfoDto userInfoDto = BeanUtil.copyProperties(userInfo, UserInfoDto.class);

        List<UserRole> userRoleList = userRoleService.findByUserId(userInfo.getId());
        List<Long> roleIdList = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<Role> roles = roleService.listByIds(roleIdList);
        userInfoDto.setRoleList(BeanUtil.copyToList(roles, RoleDto.class));

        return userInfoDto;
    }

}
