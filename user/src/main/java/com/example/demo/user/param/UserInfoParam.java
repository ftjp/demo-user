package com.example.demo.user.param;

import lombok.Data;
import lombok.experimental.Accessors;


@Accessors(chain = true)
@Data
public class UserInfoParam {

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

}