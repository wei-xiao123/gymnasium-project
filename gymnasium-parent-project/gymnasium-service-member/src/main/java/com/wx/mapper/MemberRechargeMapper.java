package com.wx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wx.pojo.member_recharge.MemberRecharge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberRechargeMapper extends BaseMapper<MemberRecharge> {

    IPage<MemberRecharge> getRechargeList(IPage<MemberRecharge> page);

    IPage<MemberRecharge> getRechargeByMember(IPage<MemberRecharge> page,@Param("memberId") Long memberId);
}
