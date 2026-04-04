package com.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.mapper.GoodsMapper;
import com.wx.pojo.goods.Goods;
import com.wx.pojo.goods.GoodsParam;
import com.wx.service.goods.GoodsService;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(interfaceClass = GoodsService.class)
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

	@Override
	public IPage<Goods> queryPage(GoodsParam param) {
		long currentPage = param.getCurrentPage() == null ? 1L : param.getCurrentPage();
		long pageSize = param.getPageSize() == null ? 10L : param.getPageSize();
		IPage<Goods> page = new Page<>(currentPage, pageSize);
		QueryWrapper<Goods> query = new QueryWrapper<>();
		if (StringUtils.isNotEmpty(param.getName())) {
			query.like("name", param.getName());
		}
		query.orderByDesc("goods_id");
		return this.baseMapper.selectPage(page, query);
	}
}
