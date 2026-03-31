package com.wx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.mapper.MemberRoleMapper;
import com.wx.pojo.member_recharge.MemberRole;
import com.wx.service.member_role.MemberRoleService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(interfaceClass = MemberRoleService.class)
public class MemberRoleServiceImpl extends ServiceImpl<MemberRoleMapper, MemberRole> implements MemberRoleService{
}
