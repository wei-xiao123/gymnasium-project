package com.wx.service.sys_user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.pojo.sys_user.PageParam;
import com.wx.pojo.sys_user.SysUser;

import java.util.List;


public interface SysUserService extends IService<SysUser> {

    IPage<SysUser> list(PageParam param);

    //根据员工姓名查询员工信息
    SysUser loadUser(String username);

    List<SysUser> getTeacherList();

}
