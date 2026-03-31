package com.wx.pojo.sys_role;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoleAssignParam implements Serializable {
    private static final long serialVersionUID = 1L;
    //用户id
    private Long userId;
    //角色id
    private Long roleId;
}
