package com.xq.web.sys_role.entity;
import lombok.Data;
@Data
public class RoleParam {
    private Long currentPage;//当前页码

    private Long pageSize;//页面数据容量

    private String roleName;//角色名称
}
