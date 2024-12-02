package com.example.demo.infruastructure.common.UserContext;

import com.example.demo.user.dto.RoleDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;


@Accessors(chain = true)
@Data
public class UserInfoContext {

    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户密码
     */
    private String userPwd;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    private Integer deleted;


    private List<RoleDto> roleList;

}