package com.wx.service.member;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wx.pojo.member.JoinParam;
import com.wx.pojo.member.Member;
import com.wx.pojo.member.PageParam;
import com.wx.pojo.member.RechargeParam;

import java.text.ParseException;

public interface MemberService extends IService<Member> {

    void addMember(Member member);
    void editMember(Member member);
    void deleteMember(Long memberId);

    void joinApply(JoinParam joinParam) throws ParseException;

    void recharge(RechargeParam param);

    IPage<Member> queryPage(PageParam param);

    //根据用户名查询会员
    Member loadUser(String username);

    boolean existsByUsername(String username);

    boolean existsByPhone(String phone);

    void registerMember(Member member);
}
