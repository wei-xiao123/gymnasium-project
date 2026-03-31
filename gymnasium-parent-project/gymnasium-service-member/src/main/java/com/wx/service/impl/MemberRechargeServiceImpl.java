package com.wx.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.mapper.MemberRechargeMapper;
import com.wx.pojo.member.RechargeParamList;
import com.wx.pojo.member_recharge.MemberRecharge;
import com.wx.service.member_recharge.MemberRechargeService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(interfaceClass = MemberRechargeService.class)
public class MemberRechargeServiceImpl extends ServiceImpl<MemberRechargeMapper, MemberRecharge> implements MemberRechargeService {

    @Override
    public IPage<MemberRecharge> getRechargeList(RechargeParamList paramList) {
        //构造分页对象
        IPage<MemberRecharge> page = new Page<>(paramList.getCurrentPage(),paramList.getPageSize());
        return this.baseMapper.getRechargeList(page);
    }

    @Override
    public IPage<MemberRecharge> getRechargeByMember(RechargeParamList paramList) {
        //构造分页对象
        IPage<MemberRecharge> page = new Page<>(paramList.getCurrentPage(),paramList.getPageSize());
        return this.baseMapper.getRechargeByMember(page,paramList.getMemberId());
    }
}
