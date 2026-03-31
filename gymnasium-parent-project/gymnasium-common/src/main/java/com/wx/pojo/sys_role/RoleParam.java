package com.wx.pojo.sys_role;

import lombok.Data;

import java.io.Serializable;

/**
 * 封装Role相关的分页查询参数
 */
@Data
public class RoleParam implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long currentPage; //当前页码
    private Long pageSize; //页面容量
    private String roleName; //角色名称
}
