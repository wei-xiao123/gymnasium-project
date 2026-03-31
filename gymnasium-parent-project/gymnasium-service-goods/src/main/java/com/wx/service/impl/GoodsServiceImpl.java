package com.wx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.mapper.GoodsMapper;
import com.wx.pojo.goods.Goods;
import com.wx.service.goods.GoodsService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(interfaceClass = GoodsService.class)
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
}
