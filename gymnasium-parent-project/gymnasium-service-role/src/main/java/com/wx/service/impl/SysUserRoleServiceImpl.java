package com.wx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.mapper.SysUserRoleMapper;
import com.wx.pojo.sys_user_role.SysUserRole;
import com.wx.service.sys_user_role.SysUserRoleService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(interfaceClass = SysUserRoleService.class)
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {
}
