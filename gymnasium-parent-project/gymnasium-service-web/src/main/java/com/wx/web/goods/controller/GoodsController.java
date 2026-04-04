package com.wx.web.goods.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wx.pojo.goods.Goods;
import com.wx.pojo.goods.GoodsParam;
import com.wx.service.goods.GoodsService;
import com.wx.utils.ResultUtils;
import com.wx.utils.ResultVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/goods")
public class GoodsController {

    @DubboReference
    GoodsService goodsService;

    //新增
    @PostMapping
    public ResultVo add(@RequestBody Goods goods){
        if(goodsService.save(goods)){
            return ResultUtils.success("新增成功");
        }
        return ResultUtils.error("新增失败");
    }

    //编辑
    @PutMapping
    public ResultVo edit(@RequestBody Goods goods){
        if(goodsService.updateById(goods)){
            return ResultUtils.success("编辑成功");
        }
        return ResultUtils.error("编辑失败");
    }

    //删除
    @DeleteMapping("/{goodsId}")
    public ResultVo delete(@PathVariable("goodsId") Long goodsId){
        if(goodsService.removeById(goodsId)) {
            return ResultUtils.success("删除成功");
        }
        return ResultUtils.error("删除失败");
    }

    //查询
    @GetMapping("/list")
    public ResultVo list(GoodsParam param){
        IPage<Goods> list = goodsService.queryPage(param);
        return ResultUtils.success("查询成功", list);
    }
}
