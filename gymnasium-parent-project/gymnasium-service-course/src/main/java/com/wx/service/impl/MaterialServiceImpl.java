package com.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.mapper.MaterialMapper;
import com.wx.pojo.equipment.ListParam;
import com.wx.pojo.equipment.Material;
import com.wx.service.equipment.MaterialService;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(interfaceClass = MaterialService.class)
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material> implements MaterialService {

	@Override
	public IPage<Material> queryPage(ListParam param) {
		long currentPage = param.getCurrentPage() == null ? 1L : param.getCurrentPage();
		long pageSize = param.getPageSize() == null ? 10L : param.getPageSize();
		IPage<Material> page = new Page<>(currentPage, pageSize);
		QueryWrapper<Material> query = new QueryWrapper<>();
		if (StringUtils.isNotEmpty(param.getName())) {
			query.like("name", param.getName());
		}
		query.orderByDesc("id");
		return this.baseMapper.selectPage(page, query);
	}
}
