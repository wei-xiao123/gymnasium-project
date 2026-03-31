package com.wx.service.sys_role;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.pojo.sys_role.RoleAssignParam;
import com.wx.pojo.sys_role.RoleParam;
import com.wx.pojo.sys_role.RolePermissionVo;
import com.wx.pojo.sys_role.SysRole;


public interface SysRoleService extends IService<SysRole> {

    IPage<SysRole> list(RoleParam roleParam);

    //查询权限数据回显
    RolePermissionVo getMenuTree(RoleAssignParam param);
}
