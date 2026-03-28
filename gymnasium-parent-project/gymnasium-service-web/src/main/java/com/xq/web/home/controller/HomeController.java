package com.xq.web.home.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xq.utils.ResultUtils;
import com.xq.utils.ResultVo;
import com.xq.web.equipment.serivce.MaterialService;
import com.xq.web.goods_order.service.GoodsOrderService;
import com.xq.web.home.entity.Echart;
import com.xq.web.home.entity.EchartItem;
import com.xq.web.home.entity.ResetPassword;
import com.xq.web.home.entity.TotalCount;
import com.xq.web.member.entity.Member;
import com.xq.web.member.service.MemberService;
import com.xq.web.suggest.entity.Suggest;
import com.xq.web.suggest.service.SuggestService;
import com.xq.web.sys_user.entity.SysUser;
import com.xq.web.sys_user.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/home")
@RestController
public class HomeController {

    @Autowired
    MemberService memberService;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    MaterialService materialService;
    @Autowired
    GoodsOrderService goodsOrderService;

    @Autowired
    SuggestService suggestService;

    //统计总数
    @GetMapping("getTotal")
    public ResultVo getTotal(){
        TotalCount totalCount = new TotalCount();
        int memberCount = memberService.count();
        totalCount.setMaterCount(memberCount);
        int userCount = sysUserService.count();
        totalCount.setUserCount(userCount);
        int materCount = materialService.count();
        totalCount.setMaterCount(materCount);
        int orderCount = goodsOrderService.count();
        totalCount.setOrderCount(orderCount);
        return ResultUtils.success("查询成功",totalCount);
    }

    //查询反馈列表
    @GetMapping("/getSuggestList")
    public ResultVo getSuggestList(){
        QueryWrapper<Suggest> query = new QueryWrapper<>();
        query.lambda().orderByDesc(Suggest::getDateTime).last("limit 3");
        List<Suggest> list = suggestService.list(query);
        return ResultUtils.success("查询成功",list);
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

    //重置密码
    @PostMapping("/resetPassword")
    public ResultVo resetPassword(@RequestBody ResetPassword resetPassword){
        if("1".equals(resetPassword.getUserType())){
            Member member = new Member();
            member.setMemberId(resetPassword.getUserId());
            member.setPassword("666666");
            memberService.updateById(member);
            return ResultUtils.success("密码重置成功!");
        }else if("2".equals(resetPassword.getUserType())){
            SysUser user = new SysUser();
            user.setUserId(resetPassword.getUserId());
            String password = DigestUtils.md5DigestAsHex("666666".getBytes());
            user.setPassword(password);
            sysUserService.updateById(user);
            return ResultUtils.success("密码重置成功!");
        }else{
            return ResultUtils.error("用户类型错误!");
        }
    }

    //修改密码
    @PostMapping("/updatePassword")
    public ResultVo updatePassword(@RequestBody ResetPassword resetPassword) {
        if ("1".equals(resetPassword.getUserType())) {//会员
            //验证原密码是否正确
            Member member1 = memberService.getById(resetPassword.getUserId());
            if (!member1.getPassword().equals(resetPassword.getOldPassword())) {
                return ResultUtils.error("原密码不正确!");
            }
            //修改
            Member member = new Member();
            member.setMemberId(resetPassword.getUserId());
            member.setPassword(resetPassword.getPassword());
            memberService.updateById(member);
            return ResultUtils.success("修改密码成功!");
        } else if ("2".equals(resetPassword.getUserType())) {//员工
            //验证原密码
            SysUser sysUser = sysUserService.getById(resetPassword.getUserId());
            String oldPas = DigestUtils.md5DigestAsHex(resetPassword.getOldPassword().getBytes());
            if(!oldPas.equals(sysUser.getPassword())){
                return ResultUtils.error("原密码不正确!");
            }
            SysUser user = new SysUser();
            user.setUserId(resetPassword.getUserId());
            String password = DigestUtils.md5DigestAsHex(resetPassword.getPassword().getBytes());
            user.setPassword(password);
            sysUserService.updateById(user);
            return ResultUtils.success("修改密码成功!");
        } else {
            return ResultUtils.error("用户类型错误!");
        }
    }
}
