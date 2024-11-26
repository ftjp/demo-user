package com.example.demo.user.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Accessors(chain = true)
@Data
public class RoleDto implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 角色名
     */
    private String roleName;

}