package com.wx.web.equipment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wx.pojo.equipment.ListParam;
import com.wx.pojo.equipment.Material;
import com.wx.service.equipment.MaterialService;
import com.wx.utils.ResultUtils;
import com.wx.utils.ResultVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/material")
public class MaterialController {

    @DubboReference
    MaterialService materialService;

    //新增
    @PostMapping
    public ResultVo add(@RequestBody Material material){
        if(materialService.save(material)){
            return ResultUtils.success("新增成功");
        }
        return ResultUtils.error("新增失败");
    }

    //编辑
    @PutMapping
    public ResultVo edit(@RequestBody Material material){
        if(materialService.updateById(material)){
            return ResultUtils.success("编辑成功");
        }
        return ResultUtils.error("编辑失败");
    }

    //删除
    @DeleteMapping("/{id}")
    public ResultVo delete(@PathVariable("id") Long id){
        if(materialService.removeById(id)){
            return ResultUtils.success("删除成功");
        }
        return ResultUtils.error("删除失败");
    }

    //查询列表
    @GetMapping("/list")
    public ResultVo list(ListParam param){
        IPage<Material> list = materialService.queryPage(param);
        return ResultUtils.success("查询成功", list);
    }
}
