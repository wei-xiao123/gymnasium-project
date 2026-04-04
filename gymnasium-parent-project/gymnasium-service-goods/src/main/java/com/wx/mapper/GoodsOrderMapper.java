package com.wx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wx.pojo.goods_order.GoodsOrder;
import com.wx.pojo.home.EchartItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsOrderMapper extends BaseMapper<GoodsOrder> {

    //查询热销商品
    List<EchartItem> hotGoods();
    //查询热销卡
    List<EchartItem> hotCards();
    //查询热销课程
    List<EchartItem> hotCourse();

    //昨日订单数
    Integer countYesterdayOrders();
}
