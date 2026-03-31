package com.wx.web.member_card.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wx.pojo.member_card.ListCard;
import com.wx.pojo.member_card.MemberCard;
import com.wx.service.member_card.MemberCardService;
import com.wx.utils.ResultUtils;
import com.wx.utils.ResultVo;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/memberCard")
public class MemberCardController {

    @DubboReference
    MemberCardService memberCardService;

    @PostMapping
    public ResultVo add( @RequestBody MemberCard memberCard){
        if(memberCardService.save(memberCard)){
            return ResultUtils.success("新增成功");
        }
        return ResultUtils.error("新增失败");
    }

    //修改
    @PutMapping
    public ResultVo edit(@RequestBody MemberCard memberCard){
        if(memberCardService.updateById(memberCard)){
            return ResultUtils.success("修改成功");
        }
        return ResultUtils.error("修改成功");
    }

    //删除
    @DeleteMapping("/{cardId}")
    public ResultVo delete(@PathVariable("cardId") Long cardId){
        if(memberCardService.removeById(cardId)){
            return ResultUtils.success("删除成功");
        }
        return ResultUtils.error("删除失败");
    }

    //分页查询会员列表信息
    @GetMapping("/list")
    public ResultVo list(ListCard listCard){
        //构造分页对象
        IPage<MemberCard> page = new Page<>(listCard.getCurrentPage(),listCard.getPageSize());
        //构造查询条件
        QueryWrapper<MemberCard> query = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(listCard.getTitle())){
            query.lambda().like(MemberCard::getTitle,listCard.getTitle());
        }
        IPage<MemberCard> list = memberCardService.page(page, query);
        return ResultUtils.success("查询成功", list);
    }
}
