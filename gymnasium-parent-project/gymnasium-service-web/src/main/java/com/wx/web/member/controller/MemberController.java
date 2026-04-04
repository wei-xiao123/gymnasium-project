package com.wx.web.member.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wx.pojo.member.*;
import com.wx.pojo.member_card.MemberCard;
import com.wx.pojo.member_recharge.MemberRecharge;
import com.wx.pojo.member_role.MemberRole;
import com.wx.service.member.MemberService;
import com.wx.service.member_card.MemberCardService;
import com.wx.service.member_recharge.MemberRechargeService;
import com.wx.service.member_role.MemberRoleService;
import com.wx.utils.ResultUtils;
import com.wx.utils.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/member")
@Slf4j
public class MemberController {

    @DubboReference
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    //新增会员
    @PostMapping
    public ResultVo add(@RequestBody Member member){
        // Dubbo 消费端避免传输 Lambda QueryWrapper，直接用服务方法查询
        Member one = memberService.loadUser(member.getUsername());
        if(one != null){
            return ResultUtils.error("会员卡号被占用!");
        }
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberService.addMember(member);
        return ResultUtils.success("会员信息新增成功");
    }

    //修改会员
    @PutMapping
    public ResultVo edit(@RequestBody Member member){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Member) {
            Member loginMember = (Member) authentication.getPrincipal();
            if (loginMember.getMemberId() == null || member.getMemberId() == null
                    || !loginMember.getMemberId().equals(member.getMemberId())) {
                return ResultUtils.error("只能修改自己的信息");
            }
            Member dbMember = memberService.getById(member.getMemberId());
            if (dbMember == null) {
                return ResultUtils.error("会员不存在");
            }
            member.setUsername(dbMember.getUsername());
            member.setStatus(dbMember.getStatus());
            MemberRole dbRole = memberRoleService.getByMemberId(member.getMemberId());
            if (dbRole != null) {
                member.setRoleId(dbRole.getRoleId());
            }
        }
        Member one = memberService.loadUser(member.getUsername());
        if(one != null && !one.getMemberId().equals(member.getMemberId())){
            return ResultUtils.error("会员卡号被占用!");
        }
        memberService.editMember(member);
        return ResultUtils.success("修改成功");
    }

    //删除
    @DeleteMapping("/{memberId}")
    public ResultVo delete(@PathVariable("memberId") Long memberId){
        memberService.deleteMember(memberId);
        return ResultUtils.success("删除成功");
    }

    //分页查询
    @GetMapping("/list")
    public ResultVo list(PageParam pageParam){
        try {
            IPage<Member> list = memberService.queryPage(pageParam);
            return ResultUtils.success("查询成功", list);
        } catch (Exception e) {
            log.error("分页查询会员失败", e);
            long currentPage = pageParam.getCurrentPage() == null ? 1L : pageParam.getCurrentPage();
            long pageSize = pageParam.getPageSize() == null ? 10L : pageParam.getPageSize();
            Page<Member> empty = new Page<>(currentPage, pageSize);
            empty.setRecords(new ArrayList<>());
            empty.setTotal(0);
            return ResultUtils.success("查询成功", empty);
        }
    }

    @DubboReference
    MemberRoleService memberRoleService;

    //根据会员id查询对应的角色id
    @GetMapping("/getRoleByMemberId")
    public ResultVo getRoleByMemberId(Long memberId){
        if(memberId == null){
            return ResultUtils.success("查询成功", null);
        }
        MemberRole one = memberRoleService.getByMemberId(memberId);
        return ResultUtils.success("查询成功",one);
    }

    @DubboReference
    MemberCardService memberCardService;

    //查询会员卡列表
    @GetMapping("/getCardList")
    public ResultVo getCardList(){
        try {
            List<MemberCard> all = memberCardService.list();
            List<MemberCard> list = new ArrayList<>();
            for (MemberCard item : all) {
                if("1".equals(item.getStatus())){
                    list.add(item);
                }
            }
            return ResultUtils.success("查询成功",list);
        } catch (Exception e) {
            log.error("查询会员卡列表失败", e);
            return ResultUtils.success("查询成功", new ArrayList<>());
        }
    }

    //办卡提交
    @PostMapping("/joinApply")
    public ResultVo joinApply(@RequestBody JoinParam joinParam) throws ParseException {
        memberService.joinApply(joinParam);
        return ResultUtils.success("办卡成功");
    }

    //充值
    @PostMapping("/recharge")
    public ResultVo recharge(@RequestBody RechargeParam param){
        memberService.recharge(param);
        return ResultUtils.success("充值成功");
    }

    @DubboReference
    MemberRechargeService memberRechargeService;

    //充值查询
    @GetMapping("getMyRecharge")
    public ResultVo getMyRecharge(RechargeParamList param){
        //判断当前用户是会员还是员工
        if(param.getUserType().equals("1")){ //会员
            IPage<MemberRecharge> list = memberRechargeService.getRechargeByMember(param);
            return ResultUtils.success("查询成功", list);
        }else if(param.getUserType().equals("2")){//员工
            IPage<MemberRecharge> list = memberRechargeService.getRechargeList(param);
            return ResultUtils.success("查询成功", list);
        }else{
            return ResultUtils.error("用户类型不存在");
        }
    }
}
