package com.wx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wx.pojo.sys_role_menu.RoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    //保存的方法
    boolean saveRoleMenu(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds);
}
