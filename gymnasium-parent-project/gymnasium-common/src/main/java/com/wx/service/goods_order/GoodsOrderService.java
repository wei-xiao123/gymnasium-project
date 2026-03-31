package com.wx.service.goods_order;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.pojo.goods_order.GoodsOrder;
import com.wx.pojo.home.EchartItem;

import java.util.List;

public interface GoodsOrderService extends IService<GoodsOrder> {

    //查询热销商品
    List<EchartItem> hotGoods();
    //查询热销卡
    List<EchartItem> hotCards();
    //查询热销课程
    List<EchartItem> hotCourse();
}
