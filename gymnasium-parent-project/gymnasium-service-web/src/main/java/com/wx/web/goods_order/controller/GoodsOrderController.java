package com.wx.web.goods_order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wx.pojo.goods.Goods;
import com.wx.pojo.goods.GoodsParam;
import com.wx.pojo.goods_order.GoodsOrder;
import com.wx.pojo.goods_order.OrderItem;
import com.wx.pojo.goods_order.OrderParam;
import com.wx.pojo.sys_user.SysUser;
import com.wx.service.goods.GoodsService;
import com.wx.service.goods_order.GoodsOrderService;
import com.wx.service.sys_user.SysUserService;
import com.wx.utils.ResultUtils;
import com.wx.utils.ResultVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class GoodsOrderController {

    @DubboReference
    private GoodsOrderService goodsOrderService;
    @DubboReference
    SysUserService sysUserService;
    @DubboReference
    GoodsService goodsService;

    //下单
    @PostMapping("/down")
    public ResultVo down(@RequestBody OrderParam param){
        //查询用户信息
        SysUser user = sysUserService.getById(param.getUserId());
        List<OrderItem> list = param.getOrderList();
        List<GoodsOrder> orderList = new ArrayList<>();
        for(int i = 0;i<list.size();i++){
            Long goodsId = list.get(i).getGoodsId();
            Integer num = list.get(i).getNum();
            //查询商品信息
            Goods goods = goodsService.getById(goodsId);
            GoodsOrder order = new GoodsOrder();
            BeanUtils.copyProperties(goods,order);
            order.setNum(list.get(i).getNum());
            BigDecimal number = BigDecimal.valueOf(list.get(i).getNum());
            BigDecimal price = goods.getPrice();
            BigDecimal total = number.multiply(price);
            BigDecimal totalPrice = total.setScale(2, BigDecimal.ROUND_HALF_UP);
            order.setTotalPrice(totalPrice);
            order.setControlUser(user.getNickName());
            orderList.add(order);
        }
        if(orderList.size() > 0){
            goodsOrderService.saveBatch(orderList);
        }
        return ResultUtils.success("下单成功");
    }

    //查询订单列表
    @GetMapping("/list")
    public ResultVo list(GoodsParam param){
        IPage<GoodsOrder> list = goodsOrderService.queryPage(param);
        return ResultUtils.success("查询成功", list);
    }


}
