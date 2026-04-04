package com.wx.web.home.controller;

import com.wx.pojo.home.Echart;
import com.wx.pojo.home.EchartItem;
import com.wx.pojo.home.ResetPassword;
import com.wx.pojo.home.TotalCount;
import com.wx.pojo.member.Member;
import com.wx.pojo.suggest.Suggest;
import com.wx.pojo.sys_user.SysUser;
import com.wx.service.equipment.MaterialService;
import com.wx.service.goods_order.GoodsOrderService;
import com.wx.service.member.MemberService;
import com.wx.service.suggest.SuggestService;
import com.wx.service.sys_user.SysUserService;
import com.wx.utils.ResultUtils;
import com.wx.utils.ResultVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api/home")
@RestController
@Slf4j
public class HomeController {

    @DubboReference
    MemberService memberService;
    @DubboReference
    SysUserService sysUserService;
    @DubboReference
    MaterialService materialService;
    @DubboReference
    GoodsOrderService goodsOrderService;

    @DubboReference
    SuggestService suggestService;

    //统计总数
    @GetMapping("getTotal")
    public ResultVo getTotal(){
        TotalCount totalCount = new TotalCount();
        int memberCount = memberService.count();
        totalCount.setMemberCount(memberCount);
        int userCount = sysUserService.count();
        totalCount.setUserCount(userCount);
        int materCount = materialService.count();
        totalCount.setMaterCount(materCount);
        int orderCount = goodsOrderService.countYesterdayOrders();
        totalCount.setOrderCount(orderCount);
        return ResultUtils.success("查询成功",totalCount);
    }

    //查询反馈列表
    @GetMapping("/getSuggestList")
    public ResultVo getSuggestList(){
        try {
            List<Suggest> list = suggestService.queryTopList(3);
            return ResultUtils.success("查询成功",list);
        } catch (Exception e) {
            log.error("查询反馈列表失败", e);
            return ResultUtils.success("查询成功", new ArrayList<>());
        }
    }

    //查询热销商品
    @GetMapping("/getHotGoods")
    public ResultVo getHotGoods(){
        List<EchartItem> echartItems = goodsOrderService.hotGoods();
        Echart echart = new Echart();
        if(echartItems.size() > 0){
            for(int i = 0;i<echartItems.size();i++){
                echart.getNames().add(echartItems.get(i).getName());
                echart.getValues().add(echartItems.get(i).getValue());
            }
        }
        return ResultUtils.success("查询成功",echart);
    }

    //热销卡查询
    @GetMapping("/getHotCards")
    public ResultVo getHotCards(){
        List<EchartItem> echartItems = goodsOrderService.hotCards();
        return ResultUtils.success("查询成功",echartItems);
    }

    //热销课程查询
    @GetMapping("/getHotCourse")
    public ResultVo getHotCourse(){
        List<EchartItem> echartItems = goodsOrderService.hotCourse();
        return ResultUtils.success("查询成功",echartItems);
    }

    @Autowired
    PasswordEncoder passwordEncoder;

    //重置密码
    @PostMapping("/resetPassword")
    public ResultVo resetPassword(@RequestBody ResetPassword resetPassword){
        if(resetPassword.getUserType().equals("1")){ // 会员
            Member member = new Member();
            member.setMemberId(resetPassword.getUserId());
            member.setPassword(passwordEncoder.encode("666666")); //密码统一重置成666666
            memberService.updateById(member);
            return ResultUtils.success("密码重置成功");
        }else if(resetPassword.getUserType().equals("2")){ //员工
            SysUser sysUser = new SysUser();
            sysUser.setUserId(resetPassword.getUserId());
            //String password = DigestUtils.md5DigestAsHex("666666".getBytes());
            sysUser.setPassword(passwordEncoder.encode("666666"));
            sysUserService.updateById(sysUser);
            return ResultUtils.success("密码重置成功");
        }else{ // 用户类型错误
            return ResultUtils.error("用户类型错误");
        }
    }

    //修改密码
    @PostMapping("/updatePassword")
    public ResultVo updatePassword(@RequestBody ResetPassword resetPassword){
        if(resetPassword.getUserType().equals("1")){ //会员
            //验证原始密码是否正确
            Member member1 = memberService.getById(resetPassword.getUserId());
           /* if(!member1.getPassword().equals(resetPassword.getOldPassword())){
                return ResultUtils.error("原密码不正确");
            }*/
            String dbPassword= member1.getPassword();
            if(!passwordEncoder.matches(resetPassword.getOldPassword(),dbPassword)){
                return ResultUtils.error("原密码不正确");
            }
            //执行修改密码的操作
            Member member = new Member();
            member.setMemberId(resetPassword.getUserId());
            //member.setPassword(resetPassword.getPassword());
            member.setPassword(passwordEncoder.encode(resetPassword.getPassword()));
            memberService.updateById(member);
            return ResultUtils.success("修改密码成功");
        }else if(resetPassword.getUserType().equals("2")){ //员工
            //验证原来的密码
            SysUser sysUser = sysUserService.getById(resetPassword.getUserId());
            String oldPas = DigestUtils.md5DigestAsHex(resetPassword.getOldPassword().getBytes());
            /*if(!oldPas.equals(sysUser.getPassword())){
                return ResultUtils.error("原密码不正确");
            }*/
            String dbPassword = sysUser.getPassword();
            if(!passwordEncoder.matches(resetPassword.getOldPassword(), dbPassword)){
                return ResultUtils.error("原密码不正确");
            }
            SysUser user = new SysUser();
            user.setUserId(resetPassword.getUserId());
            //String password = DigestUtils.md5DigestAsHex(resetPassword.getPassword().getBytes());
            user.setPassword(passwordEncoder.encode(resetPassword.getPassword()));
            sysUserService.updateById(user);
            return ResultUtils.success("修改密码成功");
        }else{ // 用户类型错误
            return ResultUtils.error("用户类型错误");
        }
    }

    //退出登录
    @PostMapping("loginOut")
    public ResultVo loginOut(HttpServletRequest request, HttpServletResponse response){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }
        return new ResultUtils().success("退出登录成功");
    }
}
