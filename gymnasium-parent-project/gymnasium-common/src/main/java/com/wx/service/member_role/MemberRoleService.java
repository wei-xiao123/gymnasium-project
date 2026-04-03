package com.wx.service.member_role;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.pojo.member_role.MemberRole;

public interface MemberRoleService extends IService<MemberRole> {

	MemberRole getByMemberId(Long memberId);
}
