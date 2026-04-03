package com.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.mapper.GoodsOrderMapper;
import com.wx.pojo.goods.GoodsParam;
import com.wx.pojo.goods_order.GoodsOrder;
import com.wx.pojo.home.EchartItem;
import com.wx.service.goods_order.GoodsOrderService;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

@DubboService(interfaceClass = GoodsOrderService.class)
public class GoodsOrderServiceImpl extends ServiceImpl<GoodsOrderMapper, GoodsOrder> implements GoodsOrderService {

    @Override
    public IPage<GoodsOrder> queryPage(GoodsParam param) {
        long currentPage = param.getCurrentPage() == null ? 1L : param.getCurrentPage();
        long pageSize = param.getPageSize() == null ? 10L : param.getPageSize();
        IPage<GoodsOrder> page = new Page<>(currentPage, pageSize);
        QueryWrapper<GoodsOrder> query = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(param.getName())) {
            query.like("name", param.getName());
        }
        return this.baseMapper.selectPage(page, query);
    }

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
