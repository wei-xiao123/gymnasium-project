package com.wx.pojo.sys_role;

import com.wx.pojo.sys_menu.SysMenu;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class RolePermissionVo implements Serializable {
    private static final long serialVersionUID = 1L;
    //当前登录系统用户的菜单数据
    List<SysMenu> listmenu = new ArrayList();
    //角色原来分配的菜单数据
    private Object[] checkList;
}
