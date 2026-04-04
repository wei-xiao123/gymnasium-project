package com.wx.web.lost.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wx.pojo.lost.Lost;
import com.wx.pojo.lost.LostParam;
import com.wx.service.lost.LostService;
import com.wx.utils.ResultUtils;
import com.wx.utils.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lost")
@Slf4j
public class LostController {
    @DubboReference
    private LostService lostService;

    //新增
    @PostMapping
    public ResultVo add(@RequestBody Lost lost){
        if(lostService.save(lost)){
            return ResultUtils.success("插入成功!");
        }
        return ResultUtils.error("插入失败!");
    }

    //编辑
    @PutMapping
    public ResultVo edit(@RequestBody Lost lost){
        if(lostService.updateById(lost)){
            return ResultUtils.success("编辑成功!");
        }
        return ResultUtils.error("编辑失败!");
    }

    //删除
    @DeleteMapping("/{lostId}")
    public ResultVo delete(@PathVariable("lostId") Long lostId){
        if(lostService.removeById(lostId)){
            return ResultUtils.success("删除成功!");
        }
        return ResultUtils.error("删除失败!");
    }

    //列表查询
    @GetMapping("/list")
    public ResultVo list(LostParam param){
        log.info("[lost-v2] list queryPage called, currentPage={}, pageSize={}, lostName={}", param.getCurrentPage(), param.getPageSize(), param.getLostName());
        IPage<Lost> list = lostService.queryPage(param);
        return ResultUtils.success("查询成功", list);
    }
}
