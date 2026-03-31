package com.wx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wx.pojo.member.Member;
import com.wx.pojo.member.RechargeParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper extends BaseMapper<Member> {

    int addMoney(@Param("param") RechargeParam param);

    void subMoney(@Param("param") RechargeParam param);
}
