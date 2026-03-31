package com.wx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.mapper.MemberApplyMapper;
import com.wx.pojo.member_apply.MemberApply;

import com.wx.service.member_apply.MemberApplyService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(interfaceClass = MemberApplyService.class)
public class MemberApplyServiceImpl extends ServiceImpl<MemberApplyMapper, MemberApply> implements MemberApplyService {
}
