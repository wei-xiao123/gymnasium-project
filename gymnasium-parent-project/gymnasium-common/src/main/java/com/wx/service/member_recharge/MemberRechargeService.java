package com.wx.service.member_recharge;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.pojo.member.RechargeParamList;
import com.wx.pojo.member_recharge.MemberRecharge;

public interface MemberRechargeService  extends IService<MemberRecharge> {

    IPage<MemberRecharge> getRechargeList(RechargeParamList paramList);

    IPage<MemberRecharge> getRechargeByMember(RechargeParamList paramList);
}
