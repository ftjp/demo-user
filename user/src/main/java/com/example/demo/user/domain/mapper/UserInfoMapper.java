package com.example.demo.user.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.user.domain.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author LJP
 * @date 2024/11/26 9:49
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

}