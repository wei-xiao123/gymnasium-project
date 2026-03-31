package com.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.mapper.SysRoleMapper;
import com.wx.pojo.sys_menu.MakeMenuTree;
import com.wx.pojo.sys_menu.SysMenu;
import com.wx.pojo.sys_role.RoleAssignParam;
import com.wx.pojo.sys_role.RoleParam;
import com.wx.pojo.sys_role.RolePermissionVo;
import com.wx.pojo.sys_role.SysRole;
import com.wx.pojo.sys_user.SysUser;
import com.wx.service.sys_menu.SysMenuService;
import com.wx.service.sys_role.SysRoleService;
import com.wx.service.sys_user.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@DubboService(interfaceClass = SysRoleService.class)
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Override
    public IPage<SysRole> list(RoleParam param) {
        //构造分页对象
        IPage<SysRole> page = new Page<>();
        page.setSize(param.getPageSize());
        page.setCurrent(param.getCurrentPage());
        //构造查询条件
        QueryWrapper<SysRole> query = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(param.getRoleName())){
            query.lambda().like(SysRole::getRoleName,param.getRoleName());
        }
        return this.baseMapper.selectPage(page,query);
    }

    @DubboReference
    SysUserService sysUserService;

    @DubboReference
    SysMenuService sysMenuService;

    @Override
    public RolePermissionVo getMenuTree(RoleAssignParam param) {
        //查询用户信息
        SysUser user = sysUserService.getById(param.getUserId());
        List<SysMenu> list = null;
        if(StringUtils.isNotEmpty(user.getIsAdmin()) && user.getIsAdmin().equals("1")){
            //超级管理员，直接查询所有的菜单信息
            list = sysMenuService.list();
        }else{
            list = sysMenuService.getMenuByUserId(user.getUserId());
        }
        //组装树形数据
        List<SysMenu> menuList = MakeMenuTree.makeTree(list,0L);
        //查询角色原来的菜单分配信息
        List<SysMenu> roleList = sysMenuService.getMenuByRoleId(param.getRoleId());
        List<Long> ids = new ArrayList<>();
        Optional.ofNullable(roleList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> item != null)
                .forEach(item -> {
                    ids.add(item.getMenuId());
                });
        //组装数据
        RolePermissionVo vo = new RolePermissionVo();
        vo.setListmenu(menuList);
        vo.setCheckList(ids.toArray());
        return vo;
    }
}
