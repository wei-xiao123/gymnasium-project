package com.wx.pojo.sys_role_menu;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户发送分配菜单权限的请求，携带参数的封装
 */
@Data
public class SaveMenuParam implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long roleId; // 角色id
    private List<Long> list; //复选菜单id的集合
}
