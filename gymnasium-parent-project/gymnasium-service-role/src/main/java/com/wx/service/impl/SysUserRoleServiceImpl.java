package com.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.mapper.SysUserRoleMapper;
import com.wx.pojo.sys_user_role.SysUserRole;
import com.wx.service.sys_user_role.SysUserRoleService;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

@DubboService(interfaceClass = SysUserRoleService.class)
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

	@Override
	public SysUserRole getByUserId(Long userId) {
		if (userId == null) {
			return null;
		}
		QueryWrapper<SysUserRole> query = new QueryWrapper<>();
		query.eq("user_id", userId);
		List<SysUserRole> list = this.baseMapper.selectList(query);
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
}
