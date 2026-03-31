package com.wx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.mapper.GoodsOrderMapper;
import com.wx.pojo.goods_order.GoodsOrder;
import com.wx.pojo.home.EchartItem;
import com.wx.service.goods_order.GoodsOrderService;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

@DubboService(interfaceClass = GoodsOrderService.class)
public class GoodsOrderServiceImpl extends ServiceImpl<GoodsOrderMapper, GoodsOrder> implements GoodsOrderService {

    @Override
    public List<EchartItem> hotGoods() {
        return this.baseMapper.hotGoods();
    }

    @Override
    public List<EchartItem> hotCards() {
        return this.baseMapper.hotCards();
    }

    @Override
    public List<EchartItem> hotCourse() {
        return this.baseMapper.hotCourse();
    }
}
