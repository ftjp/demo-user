package com.example.demo.user.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.example.demo.infruastructure.common.UserContext.UserInfoContext;
import com.example.demo.infruastructure.util.DataRedisOptionService;
import com.example.demo.infruastructure.util.ExceptionUtil;
import com.example.demo.user.auth.ResidConstanst;
import com.example.demo.user.domain.entity.Role;
import com.example.demo.user.domain.entity.UserInfo;
import com.example.demo.user.domain.entity.UserRole;
import com.example.demo.user.domain.service.RoleService;
import com.example.demo.user.domain.service.UserInfoService;
import com.example.demo.user.domain.service.UserRoleService;
import com.example.demo.user.dto.RoleDto;
import com.example.demo.user.dto.UserInfoDto;
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
    @Resource
    private DataRedisOptionService dataRedisOptionService;


    public UserInfoDto getUserInfoById(Long userId) {
        UserInfo userInfo = userInfoService.findById(userId);
        return BeanUtil.copyProperties(userInfo, UserInfoDto.class);
    }

    public UserInfoDto getExtendUserInfoById(Long userId) {
        UserInfo userInfo = userInfoService.findById(userId);
        return userInfo == null ? null : getExtendUserInfo(userInfo);
    }

    public UserInfoDto getUserInfoByName(String userName) {
        UserInfo userInfo = userInfoService.findByName(userName);
        return BeanUtil.copyProperties(userInfo, UserInfoDto.class);
    }

    public UserInfoDto getExtendUserInfoByName(String userName) {
        UserInfo userInfo = userInfoService.findByName(userName);
        return userInfo == null ? null : getExtendUserInfo(userInfo);
    }

    public UserInfoDto getExtendUserInfo(UserInfo userInfo) {
        UserInfoDto userInfoDto = BeanUtil.copyProperties(userInfo, UserInfoDto.class);

        List<UserRole> userRoleList = userRoleService.findByUserId(userInfo.getId());
        if (CollUtil.isEmpty(userRoleList)) {
            return userInfoDto;
        }
        List<Long> roleIdList = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<Role> roles = roleService.listByIds(roleIdList);
        userInfoDto.setRoleList(BeanUtil.copyToList(roles, RoleDto.class));

        return userInfoDto;
    }

    public UserInfoContext buildUserContext(String userName) {

        UserInfoDto userInfoDto = getExtendUserInfoByName(userName);
        ExceptionUtil.throwIfTrue(userInfoDto == null, "用户不存在");
        UserInfoContext userInfoContext = BeanUtil.copyProperties(userInfoDto, UserInfoContext.class);

        dataRedisOptionService.set(ResidConstanst.USER_CONTEXT_PREFIX + userInfoContext.getUserName(), userInfoContext);
        return userInfoContext;
    }

}
