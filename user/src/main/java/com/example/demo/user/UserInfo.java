package com.example.demo.user;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author caili
 * @create 2024/11/15 17:32
 */
@Data
@Accessors(chain = true)
public class UserInfo {

    private String username;
    private Integer age;
    private String email;
    private String address;
    private String password;
    private List<Role> roles;

}
