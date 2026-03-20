package com.xq.web.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xq.web.member.entity.Member;

public interface MemberService extends IService<Member> {

    void addMember(Member member);
    void editMember(Member member);
    void deleteMember(Long memberId);

}
