package com.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.mapper.LostMapper;
import com.wx.pojo.lost.Lost;
import com.wx.pojo.lost.LostParam;
import com.wx.service.lost.LostService;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(interfaceClass = LostService.class)
public class LostServiceImpl extends ServiceImpl<LostMapper, Lost> implements LostService{

	@Override
	public IPage<Lost> queryPage(LostParam param) {
		long currentPage = param.getCurrentPage() == null ? 1L : param.getCurrentPage();
		long pageSize = param.getPageSize() == null ? 10L : param.getPageSize();
		IPage<Lost> page = new Page<>(currentPage, pageSize);
		QueryWrapper<Lost> query = new QueryWrapper<>();
		if (StringUtils.isNotEmpty(param.getLostName())) {
			query.like("lost_name", param.getLostName());
		}
		query.orderByDesc("lost_id");
		return this.baseMapper.selectPage(page, query);
	}
}