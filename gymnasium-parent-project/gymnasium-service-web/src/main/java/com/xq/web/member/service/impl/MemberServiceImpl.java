package com.xq.web.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xq.web.member.entity.Member;
import com.xq.web.member.mapper.MemberMapper;
import com.xq.web.member.service.MemberService;
import com.xq.web.member_role.entity.MemberRole;
import com.xq.web.member_role.service.MemberRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Autowired
    private MemberRoleService memberRoleService;

    @Override
    @Transactional
    public void addMember(Member member) {
        //新增会员
        int insert = this.baseMapper.insert(member);
        //设置角色信息
        if(insert > 0){
            MemberRole role = new MemberRole();
            role.setMemberId(member.getMemberId());
            role.setRoleId(member.getRoleId());
            memberRoleService.save(role);
        }
    }

    @Override
    @Transactional
    public void editMember(Member member) {
        //编辑会员本身
        int i = this.baseMapper.updateById(member);
        if(i>0){
            //删除原有的旧的关联信息
            QueryWrapper<MemberRole> query = new QueryWrapper();
            query.lambda().eq(MemberRole::getMemberId,member.getMemberId());
            memberRoleService.remove(query);
            //新建新的关联信息
            MemberRole role = new MemberRole();
            role.setMemberId(member.getMemberId());
            role.setRoleId(member.getRoleId());
            memberRoleService.save(role);
        }
    }

    @Override
    public void deleteMember(Long memberId) {
        //删除会员本身的信息
        int i = this.baseMapper.deleteById(memberId);
        //删除关联关系
        if(i>0){
            QueryWrapper<MemberRole> query = new QueryWrapper();
            query.lambda().eq(MemberRole::getMemberId,memberId);
            this.memberRoleService.remove(query);
        }
    }
}
