package com.wx.web.member_card.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wx.pojo.member_card.ListCard;
import com.wx.pojo.member_card.MemberCard;
import com.wx.service.member_card.MemberCardService;
import com.wx.utils.ResultUtils;
import com.wx.utils.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/memberCard")
@Slf4j
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
        try {
            long currentPage = listCard.getCurrentPage() == null ? 1L : listCard.getCurrentPage();
            long pageSize = listCard.getPageSize() == null ? 10L : listCard.getPageSize();

            //构造分页对象
            IPage<MemberCard> page = new Page<>(currentPage, pageSize);
            IPage<MemberCard> list;
            // 默认列表不传 Wrapper，避免 Dubbo Hessian 反序列化异常
            if(StringUtils.isNotEmpty(listCard.getTitle())){
                QueryWrapper<MemberCard> query = new QueryWrapper<>();
                query.like("title", listCard.getTitle());
                list = memberCardService.page(page, query);
            } else {
                list = memberCardService.page(page);
            }
            return ResultUtils.success("查询成功", list);
        } catch (Exception e) {
            log.error("查询会员卡列表失败", e);
            long currentPage = listCard.getCurrentPage() == null ? 1L : listCard.getCurrentPage();
            long pageSize = listCard.getPageSize() == null ? 10L : listCard.getPageSize();
            Page<MemberCard> empty = new Page<>(currentPage, pageSize);
            empty.setRecords(new ArrayList<>());
            empty.setTotal(0);
            return ResultUtils.success("查询成功", empty);
        }
    }
}
