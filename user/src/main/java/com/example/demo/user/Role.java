package com.example.demo.user;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author caili
 * @create 2024/11/15 17:32
 */
@Data
@Accessors(chain = true)
public class Role {

    private Integer id;
    private String roleName;
    private String roleDesc;

}
