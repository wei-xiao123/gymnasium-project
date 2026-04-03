package com.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.mapper.MemberRoleMapper;
import com.wx.pojo.member_role.MemberRole;
import com.wx.service.member_role.MemberRoleService;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

@DubboService(interfaceClass = MemberRoleService.class)
public class MemberRoleServiceImpl extends ServiceImpl<MemberRoleMapper, MemberRole> implements MemberRoleService{

	@Override
	public MemberRole getByMemberId(Long memberId) {
		if (memberId == null) {
			return null;
		}
		QueryWrapper<MemberRole> query = new QueryWrapper<>();
		query.eq("member_id", memberId);
		List<MemberRole> list = this.baseMapper.selectList(query);
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
}
