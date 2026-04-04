package com.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.mapper.SuggestMapper;
import com.wx.pojo.suggest.Suggest;
import com.wx.pojo.suggest.SuggestParam;
import com.wx.service.suggest.SuggestService;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

@DubboService(interfaceClass = SuggestService.class)
public class SuggestServiceImpl extends ServiceImpl<SuggestMapper, Suggest> implements SuggestService {

	@Override
	public IPage<Suggest> queryPage(SuggestParam param) {
		long currentPage = param.getCurrentPage() == null ? 1L : param.getCurrentPage();
		long pageSize = param.getPageSize() == null ? 10L : param.getPageSize();
		IPage<Suggest> page = new Page<>(currentPage, pageSize);
		QueryWrapper<Suggest> query = new QueryWrapper<>();
		if (StringUtils.isNotEmpty(param.getTitle())) {
			query.like("title", param.getTitle());
		}
		query.orderByDesc("date_time");
		return this.baseMapper.selectPage(page, query);
	}

	@Override
	public List<Suggest> queryTopList(int limit) {
		int top = limit <= 0 ? 3 : limit;
		QueryWrapper<Suggest> query = new QueryWrapper<>();
		query.orderByDesc("date_time").last("limit " + top);
		return this.baseMapper.selectList(query);
	}
}